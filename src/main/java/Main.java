/*
 * Copyright (c) 2019 Diego Urrutia-Astorga http://durrutia.cl.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * The Web Server.
 *
 * @author Diego Urrutia-Astorga.
 * @version 0.0.1
 */
public final class Main {

    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * The Port
     */
    private static final int PORT = 9000;

    private static ArrayList<ChatMessage> bdChat = new ArrayList<ChatMessage>();

    /**
     * The Ppal.
     */
    public static void main(final String[] args) throws IOException {

        log.debug("Starting the Main ..");

        // The Server Socket
        final ServerSocket serverSocket = new ServerSocket(PORT);

        // serverSocket.setReuseAddress(true);
        log.debug("Server started in port {}, waiting for connections ..", PORT);

        // Forever serve.
        while (true) {

            // One socket by request
            try  {
                RunConection run = new RunConection(serverSocket.accept(), bdChat);
                Thread hilo = new Thread(run);
                //attend the request into a thread.
                hilo.start();

            } catch (Exception e) {
                log.error("Error", e);
                throw e;
            }

        }

    }



}
