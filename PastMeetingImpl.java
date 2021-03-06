import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
/**
* A meeting that was held in the past.
*
* It includes your notes about what happened and what was agreed.
*/
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    
    private String notes = "";
   
    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes){
        super(id, date, contacts);
        if (notes == null)
            throw new NullPointerException("Meeting notes null");
        this.notes = notes;
    }
    /**
    * Returns the notes from the meeting.
    *
    * If there are no notes, the empty string is returned.
    *
    * @return the notes from the meeting.
    */
    public String getNotes(){
        return this.notes;
    }


}
