import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Set;
import java.util.LinkedHashSet;
/**
* A class to represent meetings
*
* Meetings have unique IDs, scheduled date and a list of participating contacts
*/
public abstract class MeetingImpl implements Meeting {
    
    private Calendar date;
    private int id;
    private Set<Contact> contacts;

    public MeetingImpl(int id, Calendar date,Set<Contact> contacts ){
    }

    /**
    * Returns the id of the meeting.
    *
    * @return the id of the meeting.
    */
    public int getId(){
        return 1;
    }
    /**
    * Return the date of the meeting.
    *
    * @return the date of the meeting.
    */
    public Calendar getDate(){
       Calendar calendar = new GregorianCalendar(2016,10,1);
        return calendar;
    }
    /**
    * Return the details of people that attended the meeting.
    *
    * The list contains a minimum of one contact (if there were
    * just two people: the user and the contact) and may contain an
    * arbitrary number of them.
    *
    * @return the details of people that attended the meeting.
    */
    public Set<Contact> getContacts(){
        LinkedHashSet dummySet = new LinkedHashSet();
        Contact c = new ContactImpl(1,"test", "test");
        dummySet.add(c);
        return dummySet;
    }
}
