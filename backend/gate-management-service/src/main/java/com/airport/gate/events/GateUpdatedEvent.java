package com.airport.gate.events;
package com.airport.gate.events;

import com.airport.gate.model.GateStatus;

public class GateUpdatedEvent {
    private Long gateId;
    private String gateNumber;
    private GateStatus status;
    private Long flightId; // optional, populated when assignment changes

    public GateUpdatedEvent() {}

    public GateUpdatedEvent(Long gateId, String gateNumber, GateStatus status, Long flightId) {
        this.gateId = gateId;
        this.gateNumber = gateNumber;
        this.status = status;
        this.flightId = flightId;
    }

    public Long getGateId() { return gateId; }
    public String getGateNumber() { return gateNumber; }
    public GateStatus getStatus() { return status; }
    public Long getFlightId() { return flightId; }
}
import com.airport.gate.model.GateStatus;

public class GateUpdatedEvent {
    private Long gateId;
    private String gateNumber;
    private GateStatus status;
    private Long flightId; // optional, populated when assignment changes

    public GateUpdatedEvent() {}

    public GateUpdatedEvent(Long gateId, String gateNumber, GateStatus status, Long flightId) {
        this.gateId = gateId;
        this.gateNumber = gateNumber;
        this.status = status;
        this.flightId = flightId;
    }

    public Long getGateId() { return gateId; }
    public String getGateNumber() { return gateNumber; }
    public GateStatus getStatus() { return status; }
    public Long getFlightId() { return flightId; }
}
