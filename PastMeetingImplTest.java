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
        LinkedHashSet lhs = new LinkedHashSet();
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
}
