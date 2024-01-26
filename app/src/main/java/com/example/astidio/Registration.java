package com.example.astidio;

public class Registration {
    private String danceName;
    private String id;
    private String timeStart;
    private String timeEnd;
    private String date;
    private String teacherImageResId;
    private String teacherName;

    public Registration(String id, String date, String danceName, String timeStart,String timeEnd, String teacherImageResId, String teacherName) {
        this.danceName = danceName;
        this.id = id;
        this.date = date;
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
        this.teacherImageResId = teacherImageResId;
        this.teacherName = teacherName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
