package com.example.astidio;

public class Timetable {
    private String danceName;
    private String timeStart;
    private String timeEnd;
    private String date;
    private String teacherImageResId;
    private String teacherName;
    private int availableSeats;

    public Timetable(String date, String danceName, String timeStart,String timeEnd, String teacherImageResId, String teacherName, int availableSeats) {
        this.danceName = danceName;
        this.date = date;
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
        this.teacherImageResId = teacherImageResId;
        this.teacherName = teacherName;
        this.availableSeats = availableSeats;
    }

    public String getDanceName() {
        return danceName;
    }

    public String getTeacherImageResId() {
        return teacherImageResId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
    public void setAvailableSeats(int seats)
    {
        this.availableSeats = seats;
    }
    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
