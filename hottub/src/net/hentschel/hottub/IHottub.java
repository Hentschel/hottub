/*
 * Class: IHeater
 * 
 * Created on Apr 8, 2013
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

public interface IHottub
{

    public Temperature getCurrentTemperature();

    public Temperature getSetpointTemperature();

    public boolean isBlowerOn();

    public boolean isHeaterOn();

    public boolean isPumpOn();

    public void setTempSetpoint(Temperature t);

    public void turnBlowerOff();

    public void turnBlowerOn();

    public void turnPumpOff();

    public void turnPumpOn();

}