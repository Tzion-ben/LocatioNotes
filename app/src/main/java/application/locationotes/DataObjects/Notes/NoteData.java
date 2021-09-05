package application.locationotes.DataObjects.Notes;
/**
 * This class represents a new Note input data
 * @author Tzion Beniaminov
 */

import java.io.Serializable;

public class NoteData implements Serializable {
    private String title;
    private String description;
    private long createdTime;

    private String location;

    public NoteData() {}

    /**c'tor*/
    public NoteData(String title, String description, long createdTime) {
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
        location = null;
    }

    /**getters*/
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public long getCreatedTime() {return createdTime;}
    public String getLocation() {return location;}

    /**setters*/
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setCreatedTime(long createdTime) {this.createdTime = createdTime;}
    public void setLocation(String location) {this.location = location;}
}
