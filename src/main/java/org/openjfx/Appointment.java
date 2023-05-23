package org.openjfx;

import java.sql.Timestamp;

class Appointment {
    private Timestamp startTime;
    private Timestamp endTime;

    Appointment(Timestamp startTime, Timestamp endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    boolean intersects(long time) {
        return time >= startTime.getTime() && time <= endTime.getTime();
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }
}
