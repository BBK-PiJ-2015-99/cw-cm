/**
* A meeting that will be held.
*
* It includes your notes about what happened and what was agreed.
*/

import java.util.Set;
import java.util.Calendar; 


public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

    private int id;
    private Set<Contact> contacts;
    private String notes;
    private Calendar date;

    public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts ){
        super(id, date, contacts);
        this.id = id;
        this.contacts = contacts;
        this.notes = notes;
        this.date = date;
    }
}
