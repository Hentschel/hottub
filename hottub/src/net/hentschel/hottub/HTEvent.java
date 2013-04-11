/*
 * Class: HTEvent
 * 
 * Created on Mar 28, 2013
 * 
 */
package net.hentschel.hottub;

/**
 * <tt>HTEvent</tt> ...
 */
public enum HTEvent {
    /** turn heater on was signaled */
    CallHeaterOn,
    /** turn heater off was signaled */
    CallHeaterOff,
    /** turn heater device on */
    HeaterOn,
    /** turn heater device off */
    HeaterOff,

    /** toggle pump was signaled from button, controller etc */
    CallTogglePump,
    /** pump on was signaled from button, controller etc */
    CallPumpOn,
    /** pump off was signaled from button, controller etc */
    CallPumpOff,
    /** turn pump device on */
    PumpOn,
    /** turn pump device off */
    PumpOff,

    CallToggleBlower, CallBlowerOn, CallBlowerOff, BlowerOn, BlowerOff,

    CallSetTemperatureSetpointUp, CallSetTemperatureSetpointDown,

    /** temp update from thermometer, look at payload data */
    TemperatureUpdate,
    /** set the temp set point, look at payload data */
    SetTemperatureSetpoint,
    /** update the setpoint, setpoint as called was confirmed, look at payload data */
    SetpointUpdate,

    CallToggleBacklite, CallBackliteOn, CallBackliteOff,

    CallTogglePoolLights, CallPoolLightsOn, CallPoolLightsOff, PoolLightsOn, PoolLightsOff,
    /** start the app */
    Start,
    /** stop the app */
    Stop;

    Object data = null;

    HTEvent()
    {
    }

    public Object getData()
    {
        return this.data;
    }

    void setData(Object data)
    {
        this.data = data;
    }
}
