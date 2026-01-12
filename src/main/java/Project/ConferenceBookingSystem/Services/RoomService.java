package Project.ConferenceBookingSystem.Services;

import Project.ConferenceBookingSystem.Models.Room;
import Project.ConferenceBookingSystem.Models.RoomStatus;
import Project.ConferenceBookingSystem.Repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RoomService {

    private static final String REDIS_KEY = "rooms:available";

    private final RoomRepository roomRepository;
    private final RedisService redisService;

    public RoomService(RoomRepository roomRepository,
                       RedisService redisService) {
        this.roomRepository = roomRepository;
        this.redisService = redisService;
    }

    public List<Room> getAvailableRooms() {

        List<Room> cachedRooms = redisService.get(REDIS_KEY, List.class);

        if (cachedRooms != null && !cachedRooms.isEmpty()) {
            System.out.println("Returning rooms from Redis");
            return cachedRooms;
        }

        System.out.println("Redis miss, fetching from DB");
        List<Room> rooms = roomRepository.findByStatus(RoomStatus.AVAILABLE);

        if (rooms != null && !rooms.isEmpty()) {
            redisService.set(REDIS_KEY, rooms, 5, TimeUnit.MINUTES);
        }

        return rooms;
    }
}