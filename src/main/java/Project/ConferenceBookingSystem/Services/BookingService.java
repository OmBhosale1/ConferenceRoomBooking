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
}
