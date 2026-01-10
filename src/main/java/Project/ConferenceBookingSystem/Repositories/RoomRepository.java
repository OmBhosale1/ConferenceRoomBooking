package Project.ConferenceBookingSystem.Repositories;

import Project.ConferenceBookingSystem.Models.Room;
import Project.ConferenceBookingSystem.Models.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByStatus(RoomStatus status);
}
