package com.example.astidio;

public class Regs {
    private String uid;
    private String idTimetable;
    private String regDate;
    public Regs(String uid, String idTimetable, String regDate){
        this.uid = uid;
        this.idTimetable = idTimetable;
        this.regDate = regDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdTimetable() {
        return idTimetable;
    }

    public void setIdTimetable(String idTimetable) {
        this.idTimetable = idTimetable;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
