/*
 * Class: Button
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

import net.hentschel.hottub.io.IDigitalInput;
import net.hentschel.hottub.io.IOSubscriber;
import net.hentschel.hottub.io.IDigitalIO.State;

/**
 * <tt>Button</tt> ...
 */
class Button
{
    enum Event {
        Klicked, Holding,
    }

    interface Subscriber
    {
        void handle(Event evt);
    }

    final String id;

    private Application app;

    private IDigitalInput input;

    private IOSubscriber handler;

    private HTEvent clickResponse;

    /**
     * Constructs a new <tt>Button</tt>.
     * @param id 
     * @param app 
     */
    Button(String id, Application app)
    {
        this.id = id;
        this.app = app;
        this.handler = new IOSubscriber()
        {
            public void update(State state)
            {
                Button.this.update(state);
            }
        };
    }

    void init()
    {
        this.input = (IDigitalInput) this.app.getIOFor(this.id);
        this.input.setIOSubscriber(this.handler);
    }

    void setClickEventResponse(HTEvent event)
    {
        this.clickResponse = event;
    }

    private void update(State state)
    {
        synchronized (this)
        {
            // take this as 'click' for now, this should be refined
            if (State.On.equals(state))
            {
                if (this.clickResponse != null)
                {
                    this.app.debug("button " + this.id + " click ");
                    this.app.broadcast(this.clickResponse);
                }
            }
        }
    }
}
