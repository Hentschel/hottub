/*
 * Class: Temperature
 * 
 * Created on Mar 29, 2013
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

import java.io.Serializable;

/**
 * <tt>Temperature</tt> ...
 */
public class Temperature implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum Unit implements Serializable {
        Fahrenheit("F", "&#8457;", 1), Celcius("C", "&#8451;", 0.5);

        final double delta;
        public final String display;
        public final String html;

        Unit(String display, String html, double incdelta)
        {
            this.display = display;
            this.html = html;
            this.delta = incdelta;
        }
    }

    static double convertToCelcius(double value, Unit unit)
    {
        if (Unit.Fahrenheit.equals(unit))
        {
            return value;
        }
        else if (Unit.Celcius.equals(unit))
        {
            return (value - 32.000) / 1.800;
        }
        throw new RuntimeException("unsupported unit");
    }

    static double convertToFahrenheit(double value, Unit unit)
    {
        if (Unit.Fahrenheit.equals(unit))
        {
            return value;
        }
        else if (Unit.Celcius.equals(unit))
        {
            return (value * 1.800) + 32.000;
        }
        throw new RuntimeException("unsupported unit");
    }

    public static Temperature decrement(Temperature o, Temperature.Unit unit)
    {
        Temperature result = new Temperature(o);
        result.decrement(unit);
        return result;
    }

    public static Temperature increment(Temperature o, Temperature.Unit unit)
    {
        Temperature result = new Temperature(o);
        result.increment(unit);
        return result;
    }

    private double value;

    public Temperature(double value, Unit unit)
    {
        this.value = convertToFahrenheit(value, unit);
    }

    Temperature(Temperature other)
    {
        this.value = other.value;
    }

    void add(Temperature temp)
    {
        this.value += temp.getIn(Unit.Fahrenheit);
    }

    boolean biggerThan(Temperature comp, Temperature threshold)
    {
        if (threshold == null)
        {
            return this.value > comp.value;
        }
        return this.value + threshold.value > comp.value;
    }

    void decrement(Unit unit)
    {
        this.value -= convertToFahrenheit(unit.delta, unit);
    }

    public double getIn(Unit unit)
    {
        if (Unit.Fahrenheit.equals(unit))
        {
            return convertToFahrenheit(this.value, unit);
        }
        else if (Unit.Celcius.equals(unit))
        {
            return convertToCelcius(this.value, unit);
        }
        throw new RuntimeException("unsupported unit");
    }

    void increment(Unit unit)
    {
        this.value += convertToFahrenheit(unit.delta, unit);
    }

    boolean lessThan(Temperature comp, Temperature threshold)
    {
        if (threshold == null)
        {
            return this.value < comp.value;
        }
        return this.value < (comp.value + threshold.value) ;
    }

    void set(double temperature, Unit unit)
    {
        this.value = convertToFahrenheit(value, unit);
    }

    void set(Temperature other)
    {
        this.value = other.value;
    }

    void subtract(Temperature temp)
    {
        this.value -= temp.getIn(Unit.Fahrenheit);
    }

    public String toString()
    {
        return value + "F";
    }
}
