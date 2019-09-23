/*
 * UNLICENSE
 *
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to [Unlicense](http://unlicense.org).
 */

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * paralel process conection
 * @author Ignacio Rivera
 * @version 0.0.2
 */
public class RunConection implements Runnable{

    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(RunConection.class);

    /**
     * storage of chat messages
     */
    private static ArrayList<ChatMessage> BDchat;

    /**
     * the socket
     */
    private final Socket socket;

    /**
     * the constructor
     * @param socket
     * @param bdChat
     */

    public RunConection(Socket socket, ArrayList<ChatMessage> bdChat) {
        this.socket = socket;
        this.BDchat = bdChat;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
    @Override
    public void run() {
        // One socket by request (try with resources).
        try {

            // The remote connection address.
            final InetAddress address = socket.getInetAddress();

            log.debug("========================================================================================");
            log.debug("Connection from {} in port {}.", address.getHostAddress(), socket.getPort());
            processConnection(socket);
            socket.close();

        } catch (IOException e) {
            log.error("Error", e);

        }
    }

    /**
     * Process the connection.
     * cita: Urrutia, D. (2019). server (0.0.1) [Software] obtenido de http://durrutia.cl.
     * @param socket to use as source of data.
     */
    private void processConnection(final Socket socket) throws IOException {

        // Reading the inputstream
        final List<String> lines = this.readInputStreamByLines(socket);

        final String request = lines.get(0);
        log.debug("Request: {}", request);
        // if it's a POST request
        if (lines != null && !lines.isEmpty() && lines.get(0).contains("POST")){
            String cooki = lines.get(lines.size()-1);
            log.debug(cooki);
            String user = cooki.substring(cooki.indexOf("=")+1);
            user = user.substring(user.indexOf("=")+1);
            //the message
            String mensaje = user.substring(user.indexOf("=")+1);
            //the user
            user = user.substring(0,user.indexOf("&"));
            log.debug("user: {}\nmensaje: {}",user,mensaje);
            // save chat Message
            BDchat.add(new ChatMessage(LocalDateTime.now(),user,mensaje));
        }
        final PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.println("HTTP/1.1 200 OK");
        pw.println("Server: DSM v0.0.1");
        pw.println("Date: " + new Date());
        pw.println("Content-Type: text/html; charset=UTF-8");
        // pw.println("Content-Type: text/plain; charset=UTF-8");
        pw.println();

        String content = writeContent();
        pw.println("<html><form action=\"/\" method=\"post\"><body><meta http-equiv= \"refresh\" content= \"20\">"
                + content +
                "</body></form></html>");
        pw.flush();

        log.debug("Process ended.");

    }

    /**
     * Read all the input stream.
     *
     * @param socket to use to read.
     * @return all the string readed.
     */
    private List<String> readInputStreamByLines(final Socket socket) throws IOException {

        final InputStream is = socket.getInputStream();

        // The list of string readed from inputstream.
        final List<String> lines = new ArrayList<>();

        log.debug("Reading the Inputstream ..");

        BufferedInputStream inputS = new BufferedInputStream(is);

        DataInputStream in = new DataInputStream(inputS);
        byte[] buffer = new byte[1024];
        int read;
        // save the http message in a StringBuilder
        StringBuilder dataString = new StringBuilder();
        read = inputS.read(buffer);
        dataString.append(new String(buffer, 0, read, StandardCharsets.UTF_8));
        // delet empy space
        String text = dataString.toString()
                      .replaceAll("\r\n","\n")
                      .replaceAll("\n\n","");
        // split on lines and save in list
        String [] vect = text.split("\n");
        for (int i = 0; i < vect.length; i++){
            //log.debug("line[{}]: {}",i,vect[i]);
            lines.add(vect[i]);
        }

        return lines;

    }

    /**
     * load the messages on html
     * @return html code as string
     */
    private String writeContent() {
        StringBuffer bufer = new StringBuffer("");
        bufer.append("<title>CHAT GRUPOCHAT</title>")
             .append("<div><textarea rows=\"4\" cols=\"50\">");
        // fill text area with chat messages
        for (ChatMessage chat : BDchat){
            bufer.append(chat.toString()).append("\n");
        }

        bufer.append("</textarea></div>")
             .append("<input type=\"text\" name=\"user\" placeholder=\"user\">")
             .append("<input type=\"text\" name=\"mensaje\" placeholder=\"mensaje\">")
             .append("<button type=\"submit\" value=\"Submit\">Submit</button>");
        return bufer.toString();
    }
}
