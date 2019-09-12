import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(ChatMessage.class);
    private LocalDateTime timeStamp;
    private String userName;
    private String message;

    public ChatMessage(LocalDateTime timeStamp, String userName, String message) {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%1$s: %2$s\t%3$s",
                                this.userName,
                                this.message,
                                this.timeStamp.format(
                                        DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
    }
}
