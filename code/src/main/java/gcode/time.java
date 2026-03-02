package gcode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class time {

   private LocalDateTime datetime= LocalDateTime.now();

    private static final DateTimeFormatter dateformat =DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    private static final DateTimeFormatter hourformat =DateTimeFormatter.ofPattern("HH:mm:ss");


    public void setdate(int hour,int minute,int... date){
LocalDateTime  now= LocalDateTime.now();
int year,month,day;
day = now.getDayOfMonth();
month = now.getMonthValue();
year = now.getYear();
if(date.length>0) day=date[0];
if(date.length>1) month=date[1];
if(date.length>2) year=date[2];
        datetime= LocalDateTime.of(year,month,day,hour,minute);
    }
    public static String getTime(){
        LocalDateTime  now= LocalDateTime.now();
        return now.format(hourformat);
    }

        
    public static String getDatenow(){
        LocalDateTime  now= LocalDateTime.now();
        return now.format(dateformat);
    }

public boolean equal(time t) {
 if (this.toString().equals(t.toString())) return true; 
 return false;
}

    @Override
    public String toString() {
        return  datetime.format(dateformat) ;
    }
}
