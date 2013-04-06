/*
 * Class: ButtonField
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

import java.util.HashMap;
import java.util.Map;

/**
 * <tt>ButtonField</tt> ...
 */
public class ButtonField
{
    public static final String LEFT = "left";

    public static final String RIGHT = "right";

    public static final String CENTER = "center";

    public static final String TOP = "top";

    public static final String BOTTOM = "bottom";

    private static Map<String, HTEvent> clickresponses = new HashMap<String, HTEvent>();
    static
    {
        clickresponses.put(TOP, HTEvent.CallTogglePoolLights);
        clickresponses.put(CENTER, HTEvent.CallSetTemperatureSetpointUp);
        clickresponses.put(LEFT, HTEvent.CallTogglePump);
        clickresponses.put(RIGHT, HTEvent.CallToggleBlower);
        clickresponses.put(BOTTOM, HTEvent.CallSetTemperatureSetpointDown);
    }
    
    private Application app;

    private Map<String, Button> buttons;

    /**
     * Constructs a new <tt>ButtonField</tt>.
     * @param app 
     */
    public ButtonField(Application app)
    {
        this.app = app;
        this.buttons = new HashMap<String, Button>();
    }

    public void init()
    {
        this.buttons.put(TOP, new Button(TOP, app));
        this.buttons.put(LEFT, new Button(LEFT, app));
        this.buttons.put(CENTER, new Button(CENTER, app));
        this.buttons.put(RIGHT, new Button(RIGHT, app));
        this.buttons.put(BOTTOM, new Button(BOTTOM, app));

        for (Button button : buttons.values())
        {
            button.init();
            button.setClickEventResponse(clickresponses.get(button.id));
        }
    }
}
