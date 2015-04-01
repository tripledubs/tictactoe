
package controller;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JLabel;
        
public class CountDownClock implements Runnable{
    JLabel timeLabel;
    public CountDownClock(JLabel tlb){
        timeLabel = tlb;
    }
    @Override
    public void run() {
        while(true){
            Calendar cal = new GregorianCalendar();
            
            int hour = cal.get(Calendar.HOUR);
            int min = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);
            int AM_PM = cal.get(Calendar.AM_PM);
            String time="";
            if(AM_PM==0){
                time = hour+":"+ min + ":" + sec+" "+ "AM";
            }else{
                time = hour+":"+ min + ":"+ sec+" " + "PM";
            }
            timeLabel.setText(time);
        }
    }
    
}
