/*
 * Class: AbstractLCDScreen
 * 
 * Created on Mar 31, 2013
 * 
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
                    AbstractLCDScreen.this.updateBlower(true);
                    break;
                case BlowerOff:
                    AbstractLCDScreen.this.updateBlower(false);
                    break;
                case PumpOn:
                    AbstractLCDScreen.this.updatePump(true);
                    break;
                case PumpOff:
                    AbstractLCDScreen.this.updatePump(false);
                    break;
                case HeaterOn:
                    AbstractLCDScreen.this.updateHeater(true);
                    break;
                case HeaterOff:
                    AbstractLCDScreen.this.updateHeater(false);
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

    protected abstract void updateHeater(boolean b);

    protected abstract void updatePump(boolean b);

    protected abstract void updateBlower(boolean b);

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
