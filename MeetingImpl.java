import java.text.SimpleDateFormat;
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
        if(date == null || contacts == null)
            throw new NullPointerException("date or contacts are null");
        if(id <= 0)
            throw new IllegalArgumentException("Id is equal to or less than 0");
        this.id = id;
        this.date = date;
        this.contacts = contacts;
    }

    /**
    * Returns the id of the meeting.
    *
    * @return the id of the meeting.
    */
    public int getId(){
        return this.id;
    }
    /**
    * Return the date of the meeting.
    *
    * @return the date of the meeting.
    */
    public Calendar getDate(){
        return this.date;
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
        return this.contacts;
    }
    @Override
     public String toString(){
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("dd/MM/yyyy");
        return "\""+ this.getId() +  "\""+ df.format(this.getDate().getTime()) + "\""+this.getContacts().size()  +"\"";

    }

}
