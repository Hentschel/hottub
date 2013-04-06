/*
 * Class: IDigitalIO
 * 
 * Created on Mar 30, 2013
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