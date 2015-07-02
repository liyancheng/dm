package com.tbc.framework.dm;


import com.tbc.framework.util.ExecutionContext;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * The main entrance of server
 *
 * @author LIU Fangran
 */
public class DMJettyMain {

    public static void main(String[] args) throws Exception {

        Server jettyServer = new Server();

        SocketConnector conn = new SocketConnector();
        conn.setPort(8085);
        jettyServer.setConnectors(new Connector[]{conn});

        WebAppContext wah = new WebAppContext();
        wah.setContextPath("/dm");
        wah.setWar("src/main/webapp");
        jettyServer.setHandler(wah);
        jettyServer.start();


    }


}
