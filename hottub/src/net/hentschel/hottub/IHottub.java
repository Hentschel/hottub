/*
 * Class: IHeater
 * 
 * Created on Apr 8, 2013
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