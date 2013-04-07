/*
 * Class: SingleOutputDevice
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
package net.hentschel.hottub;

import java.util.Timer;
import java.util.TimerTask;

import net.hentschel.hottub.io.IDigitalOutput;
import net.hentschel.hottub.io.IDigitalIO;
import net.hentschel.hottub.io.IDigitalIO.State;

/**
 * <tt>SingleOutputDevice</tt> ...
 */
abstract class SingleOutputDevice
{

    private static final long DELAY = 500;

    protected final Application app;

    private Timer timer;

    private Runnable deviceAction;

    private TimerTask timerTask;

    private State pending;

    private HTEvent offEvent;

    private HTEvent onEvent;

    private long delay = DELAY;

    private Object lock = new Object();

    private Interlock interlock = null;
    
    /**
     * Constructs a new <tt>SingleOutputDevice</tt>.
     */
    SingleOutputDevice(Application app)
    {
        this.app = app;
        this.timer = new Timer();
        this.deviceAction = new Runnable()
        {
            public void run()
            {
                try
                {
                    synchronized (SingleOutputDevice.this.lock)
                    {
                        if (SingleOutputDevice.this.pending != null)
                        {
                            if (SingleOutputDevice.this.pending.equals(IDigitalIO.State.On))
                            {
                                SingleOutputDevice.this.app.debug(SingleOutputDevice.this.getClass().getSimpleName() + "->ON");
                                SingleOutputDevice.this.getIOPoint().setOn();
                                SingleOutputDevice.this.app.debug(SingleOutputDevice.this.getClass().getSimpleName() + "==" + SingleOutputDevice.this.getIOPoint().getState());
                                if (SingleOutputDevice.this.onEvent != null)
                                {
                                    SingleOutputDevice.this.app.broadcast(SingleOutputDevice.this.onEvent);
                                }
                            }
                            else
                            {
                                SingleOutputDevice.this.app.debug(SingleOutputDevice.this.getClass().getSimpleName() + "->OFF");
                                SingleOutputDevice.this.getIOPoint().setOff();
                                SingleOutputDevice.this.app.debug(SingleOutputDevice.this.getClass().getSimpleName() + "==" + SingleOutputDevice.this.getIOPoint().getState());
                                if (SingleOutputDevice.this.offEvent != null)
                                {
                                    SingleOutputDevice.this.app.broadcast(SingleOutputDevice.this.offEvent);
                                }
                            }
                            SingleOutputDevice.this.pending = null;
                        }
                    }
                }
                catch (Throwable e)
                {
                    System.err.println("exception in timer task:");
                    e.printStackTrace();
                }
            }
        };
    }

    protected abstract IDigitalOutput getIOPoint();

    protected abstract void init();

    private boolean interlock()
    {
        if (this.interlock != null)
        {
            return this.interlock.eval() == false;
        }
        return false;
    }

    /**
     * is this device on? 
     * @return the state
     */
    final boolean isOn()
    {
        return this.getIOPoint().isOn();
    }

    boolean isScheduledOff()
    {
        return State.Off.equals(this.pending);
    }

    boolean isScheduledOn()
    {
        return State.On.equals(this.pending);
    }

    private TimerTask newTimerTask()
    {
        this.timerTask = new TimerTask()
        {
            public void run()
            {
                SingleOutputDevice.this.deviceAction.run();
            }
        };
        return this.timerTask;
    }

    /**
     * turn this device off (after a debounce delay of 500msec)<br>
     * if the device receives a command to turn on during that time, this
     * request will be cancelled and nothing happens
     */
    protected synchronized void off()
    {
        if (!this.isOn() || this.isScheduledOff())
        {
            return;
        }
        this.setPending(IDigitalIO.State.Off);
    }

    /**
     * turn this device on (after a debounce delay) 
     * if the device receives a command to turn off during that time, this
     * request will be cancelled and nothing happens
     */
    protected synchronized void on()
    {
        if(this.interlock())
        {
            System.err.println("interlock for " + this.getClass().getSimpleName() + " not made");
            return;
        }
        
        if (this.isOn() || this.isScheduledOn())
        {
            return;
        }
        this.setPending(IDigitalIO.State.On);
    }
    
    /**
     * schedule to turn this device off in the future.
     * if the device receives a command to turn on during that time, this
     * request will be cancelled and nothing happens
     * @param delay how long to wait before turnoing on
     */
    synchronized void scheduleOff(long delay)
    {
        this.setDelay(delay);
        this.off();
        this.setDelay(DELAY);
    }

    /**
     * schedule to turn this device on in the future.
     * if the device receives a command to turn off during that time, this
     * request will be cancelled and nothing happens
     * @param delay how long to wait before turnoing on
     */
    synchronized void scheduleOn(long delay)
    {
        this.setDelay(delay);
        this.on();
        this.setDelay(DELAY);
    }

    /**
     * set the delay until which a pending on/off action will be delayed
     * @param delay
     */
    protected final void setDelay(long delay)
    {
        this.delay = delay;
    }

    public void setInterlock(Interlock interlock)
    {
        this.interlock = interlock;
    }

    /**
     * set the event to be broadcast when this device turns off
     * @param evt (can be null)
     */
    final protected void setOffEvent(HTEvent evt)
    {
        this.offEvent = evt;
    }

    /**
     * set the event to be broadcast when this device turns on
     * @param evt (can be null)
     */
    final protected void setOnEvent(HTEvent evt)
    {
        this.onEvent = evt;
    }

    private void setPending(IDigitalIO.State pending)
    {
        synchronized (this.lock)
        {
            if(this.timerTask != null)
            {
                this.timerTask.cancel();
            }

            if (this.getIOPoint().getState().equals(pending))
            {
                this.app.debug(this.getClass().getSimpleName() + ": cancelled state change to " + pending.name() + " b/c I/O state already set");        
                this.pending = null;
                return;
            }

            this.pending = pending;
            this.app.debug(this.getClass().getSimpleName() + ": pending state change to " + pending.name() + " in " + this.delay + "msec");        
            this.timer.schedule(this.newTimerTask(), this.delay);
        }
    }
}
