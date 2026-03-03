package domain;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class time {

    private LocalDateTime datetime = LocalDateTime.now();
    private LocalDateTime endDateTime = LocalDateTime.now().plusMinutes(30);
    private int maxtimeview = 45;
    private static final DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    private static final DateTimeFormatter hourformat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void setdate(int hour, int minute, int... date) {
        LocalDateTime now = LocalDateTime.now();
        int year, month, day;
        day = now.getDayOfMonth();
        month = now.getMonthValue();
        year = now.getYear();
        if (date.length > 0)
            day = date[0];
        if (date.length > 1)
            month = date[1];
        if (date.length > 2)
            year = date[2];
        datetime = LocalDateTime.of(year, month, day, hour, minute);
        endDateTime = datetime.plusMinutes(30);
    }

    public void setenddate(int minute) {
        if (minute <= maxtimeview) {
            endDateTime = datetime.plusMinutes(minute);
        } else {
            System.out.println("minute must be less than " + maxtimeview);
        }

    }

    public boolean isstart() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(datetime))
            return true;
        return false;
    }

    public boolean isend() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDateTime))
            return true;
        return false;
    }

    public String getTimerem() {
        LocalDateTime now = LocalDateTime.now();
        Duration t = Duration.between(datetime, now);
        long days = t.toDays();
        long hours = (t.toHours() % 24);
        long minutes = (t.toMinutes() % 60);
        long seconds = (t.getSeconds() % 60);
        return days + " day " + hours + " hour " + minutes + " minute " + seconds + " second";
    }

    public static String getnow() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(dateformat);
    }

    public boolean equal(time t) {
        if (this.toString().equals(t.toString()))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return datetime.format(dateformat);
    }
}
