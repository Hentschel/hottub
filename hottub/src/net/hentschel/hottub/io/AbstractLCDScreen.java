/*
 * Class: AbstractLCDScreen
 * 
 * Created on Mar 31, 2013
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

import net.hentschel.hottub.Application;
import net.hentschel.hottub.HTEvent;
import net.hentschel.hottub.HTSubscriber;
import net.hentschel.hottub.Temperature;

/**
 * <tt>AbstractLCDScreen</tt> ...
 */
abstract class AbstractLCDScreen implements ILCDScreen
{

    private Application app;

    private HTSubscriber screenHandler;

    private HTSubscriber deviceHandler;

    private HTSubscriber temperatureHandler;

    private IDigitalOutput backlite;

    /**
     * Constructs a new <tt>AbstractLCDScreen</tt>.
     */
    AbstractLCDScreen(Application app)
    {
        this.app = app;
        this.screenHandler = new HTSubscriber()
        {

            public void eventReceived(HTEvent event)
            {
                switch (event)
                {
                case CallBackliteOn:
                    AbstractLCDScreen.this.setBacklite(true);
                    break;
                case CallBackliteOff:
                    AbstractLCDScreen.this.setBacklite(false);
                    break;
                case CallToggleBacklite:
                    AbstractLCDScreen.this.toggleBacklite();
                    break;
                default:
                    break;
                }
            }
        };
        this.deviceHandler = new HTSubscriber()
        {

            public void eventReceived(HTEvent event)
            {
                switch (event)
                {
                case BlowerOn:
                    break;
                case BlowerOff:
                    break;
                case PumpOn:
                    break;
                case PumpOff:
                    break;
                case HeaterOn:
                    break;
                case HeaterOff:
                    break;
                default:
                    break;
                }
            }
        };
        this.temperatureHandler = new HTSubscriber()
        {

            public void eventReceived(HTEvent event)
            {
                switch (event)
                {
                case TemperatureUpdate:
                    AbstractLCDScreen.this.updateTemperature((Temperature) event.getData());
                    break;
                case SetpointUpdate:
                    AbstractLCDScreen.this.updateSetpoint((Temperature) event.getData());
                    break;
                default:
                    break;
                }
            }
        };
    }

    public void init()
    {
        this.backlite = (IDigitalOutput) this.app.getIOFor(BACKLITE);

        this.app.subscribe(HTEvent.CallBackliteOn, this.screenHandler);
        this.app.subscribe(HTEvent.CallBackliteOff, this.screenHandler);
        this.app.subscribe(HTEvent.CallToggleBacklite, this.screenHandler);
        this.app.subscribe(HTEvent.BlowerOn, this.deviceHandler);
        this.app.subscribe(HTEvent.BlowerOff, this.deviceHandler);
        this.app.subscribe(HTEvent.HeaterOn, this.deviceHandler);
        this.app.subscribe(HTEvent.HeaterOff, this.deviceHandler);
        this.app.subscribe(HTEvent.PumpOn, this.deviceHandler);
        this.app.subscribe(HTEvent.PumpOff, this.deviceHandler);
        this.app.subscribe(HTEvent.TemperatureUpdate, this.temperatureHandler);
        this.app.subscribe(HTEvent.SetpointUpdate, this.temperatureHandler);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.io.ILCDScreen#init()
     */
    protected boolean isBackliteOn()
    {
        return this.backlite.getState().equals(IDigitalIO.State.On);
    }

    protected void setBacklite(boolean on)
    {
        if (on)
        {
            this.backlite.setOn();
        }
        else
        {
            this.backlite.setOff();
        }

    }

    protected void toggleBacklite()
    {
        if (this.isBackliteOn())
        {
            this.setBacklite(false);
        }
        else
        {
            this.setBacklite(true);
        }
    }

    protected abstract void updateSetpoint(Temperature data);

    protected abstract void updateTemperature(Temperature data);
}
