/*
 * Class: Test
 * 
 * Created on Mar 27, 2013
 * 
 */
package net.hentschel.est.lcd.test;

import java.util.Calendar;

import net.hentschel.est.lcd.LCD;
import net.hentschel.est.lcd.LCD.Color;
import net.hentschel.est.lcd.Point;

/**
 * <tt>Test</tt> ...
 */
public class Test
{

    /**
     * Constructs a new <tt>Test</tt>.
     */
    public Test()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Test test = new Test();
        test.run();
    }

    private void run()
    {
        LCD lcd = LCD.INSTANCE;
        lcd.clear();
        lcd.flush();
        
        lcd.setContrast(50);
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int ampm = Calendar.getInstance().get(Calendar.AM_PM);
        
        String date = (month + 1) + "/" + day + "/" + year;
        String time = hour + ":" + (minute < 10 ? "0" : "") + minute + (ampm == Calendar.AM ? "AM" : "PM");

        lcd.setOrientation(LCD.Orientation.O90);
        lcd.setFont(LCD.Font.Terminal6x8);
        lcd.print( 5,  5, date);
        lcd.print( 5, 15, time);

        lcd.print( 5, 35, "Set: 102F");
        
        lcd.setFont(LCD.Font.Lucida10x16);
        Point tempXY = new Point(5, 61);
        lcd.print( tempXY, "101");
        Integer lasttemp = 101;
        
        lcd.setFont(LCD.Font.Terminal6x8);
        lcd.print( 44, 58, "o");
        
        lcd.setFont(LCD.Font.Lucida10x16);
        lcd.print( 50, 61, "F");
        
        lcd.setOrientation(LCD.Orientation.O180);
        lcd.setFont(LCD.Font.Terminal6x8);
        lcd.print( 0, 2, "Pump");
        lcd.print( 0, 24, "Set");
        lcd.print( 0, 33, "Temp");
        lcd.print( 0, 58, "Blower");

        lcd.flush();
        
        lcd.setOrientation(LCD.Orientation.O90);
        lcd.setFont(LCD.Font.Lucida10x16);

        int min = 95;
        int max = 104;

        while(true) {
            Integer temp = min + (int)(Math.random() * ((max - min) + 1));
            System.out.println("last temp: " + lasttemp + ", current temp: " + temp);

            lcd.setPenColor(Color.White);
            lcd.print(tempXY, (lasttemp < 100 ? " " + lasttemp.toString() : lasttemp.toString()));
            lcd.flush();
            
            lcd.setPenColor(Color.Black);
            lcd.print(tempXY, (temp < 100 ? " " + temp.toString() : temp.toString()));
            lcd.flush();

            lasttemp = temp;
            
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
