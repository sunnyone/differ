package cz.nkp.differ.listener;

import java.util.Date;

/**
 *
 * @author xrosecky
 */
public class Message {

    private String identifier;
    private EventType eventType;
    private ProgressType progressType;
    private String toolName;
    private int numberOfFinishedTaks;
    private Date time;
    
    public Message() {
        this.time = new Date();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public ProgressType getProgressType() {
        return progressType;
    }

    public void setProgressType(ProgressType progressType) {
        this.progressType = progressType;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public int getNumberOfFinishedTaks() {
        return numberOfFinishedTaks;
    }

    public void setNumberOfFinishedTaks(int numberOfFinishedTaks) {
        this.numberOfFinishedTaks = numberOfFinishedTaks;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
}
