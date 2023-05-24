package org.openjfx;

import java.sql.Timestamp;

class Appointment {
    private Timestamp startTime;
    private Timestamp endTime;

    Appointment(Timestamp startTime, Timestamp endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Timestamp getStart() {
        return startTime;
    }

    public Timestamp getEnd() {
        return endTime;
    }
}
