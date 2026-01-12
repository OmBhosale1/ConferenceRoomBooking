package Project.ConferenceBookingSystem.Controllers;

import Project.ConferenceBookingSystem.Models.Room;
import Project.ConferenceBookingSystem.Models.RoomStatus;
import Project.ConferenceBookingSystem.Repositories.RoomRepository;
import Project.ConferenceBookingSystem.Services.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }


}

