/*
 * Class: IDigitalIO
 * 
 * Created on Mar 30, 2013
 * 
 */
package net.hentschel.hottub.io;

import com.pi4j.io.gpio.Pin;

public interface IDigitalIO
{

    enum State {
        On, Off, Unknown;
    }

    public State getState();

    public void init();

    public boolean isOn();

    public Pin getPin();

}