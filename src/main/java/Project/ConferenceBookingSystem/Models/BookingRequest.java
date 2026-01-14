package Project.ConferenceBookingSystem.Models;

import java.time.LocalDateTime;

public class BookingRequest {
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;


    // getters & setters


    public BookingRequest(Long roomId, LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public BookingRequest() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

