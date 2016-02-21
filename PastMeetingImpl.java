import java.util.Calendar;
import java.util.Set;
/**
* A meeting that was held in the past.
*
* It includes your notes about what happened and what was agreed.
*/
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    
    String notes;

    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes){
        //test
        super(id, date, contacts);
    }
    /**
    * Returns the notes from the meeting.
    *
    * If there are no notes, the empty string is returned.
    *
    * @return the notes from the meeting.
    */
    public String getNotes(){
         return "A RANDOM note";
    }
}
