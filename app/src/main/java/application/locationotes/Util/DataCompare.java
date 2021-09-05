package application.locationotes.Util;

/**
 * Comparator for descending order of thw Notes depending on there time creation
 * @author Tzion Beniaminov
 */

import java.io.Serializable;
import java.util.Comparator;

import application.locationotes.DataObjects.Notes.NoteData;

public class DataCompare implements Serializable , Comparator<NoteData> {

    @Override
    public int compare(NoteData o1, NoteData o2) {
        if(o1.getCreatedTime() < o2.getCreatedTime() ){return 1;}
        else{return -1;}
    }
}