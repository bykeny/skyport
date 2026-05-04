package com.airport.flight.dto;
package com.airport.flight.dto;

import com.airport.flight.model.FlightStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UpdateStatusRequest {
    @NotNull
    private FlightStatus status;
    private LocalDateTime actualDeparture;

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
    public void setActualDeparture(LocalDateTime actualDeparture) { this.actualDeparture = actualDeparture; }
}
import com.airport.flight.model.FlightStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UpdateStatusRequest {
    @NotNull
    private FlightStatus status;
    private LocalDateTime actualDeparture;

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
    public void setActualDeparture(LocalDateTime actualDeparture) { this.actualDeparture = actualDeparture; }
}
