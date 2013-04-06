/*
 * Class: LCDScreen
 * 
 * Created on Mar 28, 2013
 * 
 * (c) Copyright Novellus Systems, Inc., unpublished work, created 2003
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Novellus Systems, Inc.
 * 4000 N. First Street
 * San Jose, CA
 */
package net.hentschel.hottub.io;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import net.hentschel.est.lcd.LCD;
import net.hentschel.est.lcd.LCD.Color;
import net.hentschel.est.lcd.Point;
import net.hentschel.hottub.Application;
import net.hentschel.hottub.Temperature;

/**
 * <tt>LCDScreen</tt> ...
 */
public class HWLCDScreen extends AbstractLCDScreen
{

    private static final Point POS_TEMP = new Point(5, 61);

    private static final Point POS_SET = new Point(5, 35);

    private Temperature temperature = new Temperature(40.0, Temperature.Unit.Fahrenheit);

    private Temperature setpoint = new Temperature(40.0, Temperature.Unit.Fahrenheit);;

    private TimerTask clockUpdater;

    /**
     * Constructs a new <tt>LCDScreen</tt>.
     * @param app 
     */
    HWLCDScreen(Application app)
    {
        super(app);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.io.ILCDScreen#splash()
     */
    public void splash()
    {
    }
    
    public void init()
    {
        super.init();
        this.clockUpdater = new TimerTask(){

            public void run()
            {
                HWLCDScreen.this.updateDate();
            }};
            
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this.clockUpdater, 0, 5000);
        this.updateButtons();
        this.setBacklite(true);
    }

    private synchronized void updateButtons()
    {
        LCD.INSTANCE.setOrientation(LCD.Orientation.O180);
        LCD.INSTANCE.setFont(LCD.Font.Terminal6x8);
        LCD.INSTANCE.print( 0, 2,  "Pump");
        LCD.INSTANCE.print( 0, 24, "Set");
        LCD.INSTANCE.print( 0, 33, "Temp");
        LCD.INSTANCE.print( 0, 58, "Blower");

        LCD.INSTANCE.flush();
    }

    private synchronized void updateDate()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int ampm = Calendar.getInstance().get(Calendar.AM_PM);
        
        String date = (month + 1) + "/" + day + "/" + year;
        String time = hour + ":" + (minute < 10 ? "0" : "") + minute + (ampm == Calendar.AM ? "AM" : "PM");

        LCD.INSTANCE.setOrientation(LCD.Orientation.O90);
        LCD.INSTANCE.setFont(LCD.Font.Terminal6x8);
        LCD.INSTANCE.print( 5,  5, date);
        LCD.INSTANCE.print( 5, 15, time);

        LCD.INSTANCE.flush();
    }

    protected synchronized void updateSetpoint(Temperature set)
    {
        LCD.INSTANCE.setOrientation(LCD.Orientation.O90);
        LCD.INSTANCE.setFont(LCD.Font.Terminal6x8);

        Long dtemp = Math.round(this.setpoint.getIn(Temperature.Unit.Fahrenheit));
        LCD.INSTANCE.setPenColor(Color.White);
        LCD.INSTANCE.print(POS_SET, "Set: " + (dtemp < 100 ? " " + dtemp.toString() : dtemp.toString()) + "F");
        LCD.INSTANCE.flush();

        this.setpoint = set;

        dtemp = Math.round(this.setpoint.getIn(Temperature.Unit.Fahrenheit));
        LCD.INSTANCE.setPenColor(Color.Black);
        LCD.INSTANCE.print(POS_SET, "Set: " + (dtemp < 100 ? " " + dtemp.toString() : dtemp.toString()) + "F");
        LCD.INSTANCE.flush();
    }

    protected synchronized void updateTemperature(Temperature temp)
    {
        LCD.INSTANCE.setOrientation(LCD.Orientation.O90);
        LCD.INSTANCE.setFont(LCD.Font.Lucida10x16);

        Long dtemp = Math.round(this.temperature.getIn(Temperature.Unit.Fahrenheit));
        // erase old temp
        LCD.INSTANCE.setPenColor(Color.White);
        LCD.INSTANCE.print(POS_TEMP, (dtemp < 100 ? " " + dtemp.toString() : dtemp.toString()));

        LCD.INSTANCE.setFont(LCD.Font.Terminal6x8);
        LCD.INSTANCE.print( 44, 58, "o");
        
        LCD.INSTANCE.setFont(LCD.Font.Lucida10x16);
        LCD.INSTANCE.print( 50, 61, "F");
        
        LCD.INSTANCE.flush();

        this.temperature = temp;
        dtemp = Math.round(this.temperature.getIn(Temperature.Unit.Fahrenheit));
        
        //write new temp
        LCD.INSTANCE.setPenColor(Color.Black);
        LCD.INSTANCE.print(POS_TEMP, (dtemp < 100 ? " " + dtemp.toString() : dtemp.toString()));

        LCD.INSTANCE.setFont(LCD.Font.Terminal6x8);
        LCD.INSTANCE.print( 44, 58, "o");
        
        LCD.INSTANCE.setFont(LCD.Font.Lucida10x16);
        LCD.INSTANCE.print( 50, 61, "F");
        
        LCD.INSTANCE.flush();
    }
}
