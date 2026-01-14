package Project.ConferenceBookingSystem.Repositories;

import Project.ConferenceBookingSystem.Models.Booking;
import Project.ConferenceBookingSystem.Models.Room;
import Project.ConferenceBookingSystem.Models.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // check overlap bookings (important)
    List<Booking> findByRoomAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
            Room room,
            BookingStatus status,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

}
