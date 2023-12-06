package com.example.astidio;

public class Timetable {
    private String danceName;
    private String timeInfo;
    private int teacherImageResId;
    private String teacherName;
    private int availableSeats;

    public Timetable(String danceName, String timeInfo, int teacherImageResId, String teacherName, int availableSeats) {
        this.danceName = danceName;
        this.timeInfo = timeInfo;
        this.teacherImageResId = teacherImageResId;
        this.teacherName = teacherName;
        this.availableSeats = availableSeats;
    }

    public String getDanceName() {
        return danceName;
    }

    public String getTimeInfo() {
        return timeInfo;
    }

    public int getTeacherImageResId() {
        return teacherImageResId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}
