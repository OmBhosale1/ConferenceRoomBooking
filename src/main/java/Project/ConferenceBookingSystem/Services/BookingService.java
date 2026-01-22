package Project.ConferenceBookingSystem.Services;

import Project.ConferenceBookingSystem.Models.*;
import Project.ConferenceBookingSystem.Repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;

    private static final String ROOMS_AVAILABLE_KEY = "rooms:available";

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository,
                          UserRepository userRepository,
                          RedisService redisService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.redisService = redisService;
    }

    public Booking bookRoom(String username, BookingRequest request) {

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime start = request.getStartTime();
        LocalDateTime end = request.getEndTime();

        if (start.isAfter(end) || start.isEqual(end)) {
            throw new RuntimeException("Invalid time range");
        }

        // overlap check
        List<Booking> overlaps =
                bookingRepository.findByRoomAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                        room, BookingStatus.BOOKED, end, start
                );

        if (!overlaps.isEmpty()) {
            throw new RuntimeException("Room already booked in this time slot");
        }

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setDescription(request.getDescription());

        Booking saved = bookingRepository.save(booking);

        // Optionally mark room status BOOKED (depends on your model)
        room.setStatus(RoomStatus.BOOKED);
        roomRepository.save(room);

        // IMPORTANT: invalidate cache
        redisService.delete(ROOMS_AVAILABLE_KEY);

        return saved;
    }


    public List<Booking> getMyActiveBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserAndStatusAndEndTimeAfter(
                user, BookingStatus.BOOKED, LocalDateTime.now()
        );
    }

    public void cancelBooking(String username, Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));


        if (!booking.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You cannot cancel someone else's booking");
        }

        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new RuntimeException("Booking is not active");
        }

        if (booking.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Booking already ended");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        Room room = booking.getRoom();

        boolean hasOtherActive = !bookingRepository
                .findByRoomAndStatusAndEndTimeAfter(room, BookingStatus.BOOKED, LocalDateTime.now())
                .isEmpty();

        if (!hasOtherActive) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        redisService.delete("rooms:available");
    }


}
