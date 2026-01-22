package Project.ConferenceBookingSystem.Services;


import Project.ConferenceBookingSystem.Models.*;
import Project.ConferenceBookingSystem.Repositories.BookingRepository;
import Project.ConferenceBookingSystem.Repositories.RoomRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingExpiryScheduler {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RedisService redisService;

    private static final String ROOMS_AVAILABLE_KEY = "rooms:available";

    public BookingExpiryScheduler(BookingRepository bookingRepository,
                                  RoomRepository roomRepository,
                                  RedisService redisService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.redisService = redisService;
    }

    // runs every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void expireBookings() {

        LocalDateTime now = LocalDateTime.now();

        // all bookings that ended but still marked BOOKED
        List<Booking> expired = bookingRepository
                .findByStatusAndEndTimeBefore(BookingStatus.BOOKED, now);

        if (expired.isEmpty()) return;

        for (Booking b : expired) {
            b.setStatus(BookingStatus.EXPIRED); // add EXPIRED in enum
            bookingRepository.save(b);

            Room room = b.getRoom();
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        // ✅ invalidate cache so /rooms/available refreshes from DB
        redisService.delete(ROOMS_AVAILABLE_KEY);
    }
}
