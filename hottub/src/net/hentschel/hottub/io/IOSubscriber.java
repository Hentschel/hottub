/*
 * Class: IOSubscriber
 * 
 * Created on Mar 29, 2013
 * 
 */
package net.hentschel.hottub.io;

import net.hentschel.hottub.io.IDigitalIO.State;

public interface IOSubscriber
{

    void update(State state);

}
