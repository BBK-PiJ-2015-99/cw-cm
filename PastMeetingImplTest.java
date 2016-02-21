import org.junit.*;
import static org.junit.Assert.*;
import java.util.LinkedHashSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PastMeetingImplTest {
   
    private PastMeetingImpl pm;
 
    @Before 
    public void initTests(){
        LinkedHashSet lhs = new LinkedHashSet();
        Calendar cal = new GregorianCalendar(2015,1,1);            
         pm = new PastMeetingImpl(12,cal, lhs, "Test note"); 
    }

    @Test(expected=NullPointerException.class)
    public void contactsCalNullValuePassed(){
        PastMeeting pm = new PastMeetingImpl(12,null, null, null); 
    }

    @Test(expected=IllegalArgumentException.class)
    public void idIncorrect(){
        Contact c1 = new ContactImpl(1, "Mary", " Wathing television");
        Contact c2 = new ContactImpl(2, "John", "Lion among Tigers");
        LinkedHashSet lhs = new LinkedHashSet();
        lhs.add(c1);
        lhs.add(c2);
        Calendar cal = new GregorianCalendar(2015,1,1);            
        PastMeeting pm = new PastMeetingImpl(-1, cal,lhs, "asdasdas" );
    }

    @Test(expected=NullPointerException.class)
    public void textNullValuePassed(){
        LinkedHashSet lhs = new LinkedHashSet();
        Calendar cal = new GregorianCalendar(2015,1,1);            
        PastMeeting pm = new PastMeetingImpl(12,cal, lhs, null); 
    }

    @Test
    public void getNotes(){
        assertEquals("Test note", pm.getNotes());        
    }

    @Test
    public void getId(){
        assertEquals(Integer.valueOf(12), Integer.valueOf(pm.getId()));
    }
    
    @Test
    public void getDate(){
        Calendar cal = new GregorianCalendar(2015,1,1);            
        assertEquals(cal,pm.getDate());
    }

    @Test
    public void getContacts(){
        Contact c1 = new ContactImpl(1, "Mary", " Wathing television");
        Contact c2 = new ContactImpl(2, "John", "Lion among Tigers");
        LinkedHashSet lhs = new LinkedHashSet();
        lhs.add(c1);
        lhs.add(c2);
        assertEquals(lhs, pm.getContacts());
    }

}
