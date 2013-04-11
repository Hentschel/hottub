/*
 * Class: WebServer
 * 
 * Created on Apr 6, 2013
 * 
 */
package net.hentschel.hottub.www;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.server.handler.DebugHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import com.sun.jersey.spi.container.servlet.ServletContainer;

import net.hentschel.hottub.Application;
import net.hentschel.hottub.HTEvent;
import net.hentschel.hottub.HTSubscriber;

/**
 * <tt>WebServer</tt> ...
 */
public class WebServer
{

    private class Runner implements Runnable
    {

        private Server jetty;
        private WebServer server;

        public Runner(WebServer srv)
        {
            this.server = srv;
            this.jetty = new Server(srv.port);
        }

        public void run()
        {
            try
            {
                
                // add jersey rest servlet
                ServletHolder rest = new ServletHolder(ServletContainer.class);
                rest.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
                // point to REST implementation package
                rest.setInitParameter("com.sun.jersey.config.property.packages", "net.hentschel.home.hottub.mobile.server.rest");
                ServletContextHandler restCtx = new ServletContextHandler();
                // set up the servlet context for rest and GWT
                restCtx.setContextPath("/");
                // add the rest servlet at '/htmobile/rest/*'
                restCtx.addServlet(rest, "/rest/*");
                
                // If your app isn't packaged into a WAR, you can do this instead
                WebAppContext gwt = new WebAppContext();
                //TODO: fix path before deply, or create war
                gwt.setResourceBase("./target/htmobile-1.0-SNAPSHOT");
                gwt.setDescriptor("./target/htmobile-1.0-SNAPSHOT/WEB-INF/web.xml");
                gwt.setContextPath("/htmobile");
                gwt.setParentLoaderPriority(true);
                
                // set handlers for server
                HandlerList handlers = new HandlerList();
                handlers.setHandlers(new Handler[] {
                    gwt,
//                    restCtx, 
                    new DefaultHandler(), 
                    new DebugHandler() });
                this.jetty.setHandler(handlers);
                
                // start web server
                this.jetty.setThreadPool(new QueuedThreadPool(6));
                this.jetty.start();
                this.jetty.join();
            }
            catch (InterruptedException e)
            {
                this.server.app.debug(e.getMessage());
                e.printStackTrace();
            }
            catch (Exception e)
            {
                this.server.app.debug(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private HTSubscriber start;

    private Thread thread;

    private Application app;

    private int port;

    /**
     * Constructs a new <tt>WebServer</tt>.
     * @param app 
     */
    public WebServer(Application app, int port)
    {
        this.app = app;
        this.port = port;
    }

    public void init()
    {
        this.start = new HTSubscriber()
        {
            public void eventReceived(HTEvent event)
            {
                if (WebServer.this.thread==null)
                {
                    WebServer.this.thread = new Thread(new Runner(WebServer.this));
                    WebServer.this.thread.setDaemon(true);
                    WebServer.this.thread.start();
                }
            }
        };

        this.app.subscribe(HTEvent.Start, this.start);
    }
}
