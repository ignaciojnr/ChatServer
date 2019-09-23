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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * the CHat Message
 * @author Ignacio Rivera
 * @version 0.0.3
 */
public class ChatMessage {
    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(ChatMessage.class);
    private LocalDateTime timeStamp;
    private String userName;
    private String message;

    /**
     * Constructor
     * @param timeStamp Server arrival time.
     * @param userName Username that sends the message.
     * @param message Message content.
     */
    public ChatMessage(LocalDateTime timeStamp, String userName, String message) {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.message = message;
    }

    /**
     * returns a chat message as a string with the format:
     * "userName: message   timeStamp(HH:mm:ss dd/MM/yyyy)"
     * @return
     */
    @Override
    public String toString() {
        return String.format("%1$s: %2$s\t%3$s",
                                this.userName,
                                this.message,
                                this.timeStamp.format(
                                        DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
    }
}
