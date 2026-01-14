package Project.ConferenceBookingSystem.Controllers;

import Project.ConferenceBookingSystem.Models.Booking;
import Project.ConferenceBookingSystem.Models.BookingRequest;
import Project.ConferenceBookingSystem.Services.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/createBooking")
    public Booking book(Authentication authentication,
                        @RequestBody BookingRequest request) {

        String username = authentication.getName();
        return bookingService.bookRoom(username, request);
    }

    @GetMapping("/mybookedrooms")
    public List<Booking> myBookedRooms(Authentication authentication) {
        String username = authentication.getName();
        return bookingService.getMyBookedRooms(username);
    }
}
