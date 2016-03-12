import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Calendar; 
import java.util.Locale;
import java.util.TimeZone;

public class ContactManagerImplTest {

    private  int[] addJohn3(){
        int[] ids = new int[3];
        ids[0] = cm.addNewContact("John", "Engineer");
        ids[1] = cm.addNewContact("John", "Engineer");
        ids[2] = cm.addNewContact("John", "Engineer");
        return ids;
    }
    
    private ContactManager cm;

    @Before 
    public void initTest(){
        try{
            File f = new File("Contacts.csv");
            boolean result = Files.deleteIfExists(f.toPath());
            if(result)
                System.out.println("Deleted an existing Contacts file.");
        } catch (IOException io){
            
        } 
        cm = new ContactManagerImpl();
    }

    @Test
    public void addAContactGetById(){
        int id = cm.addNewContact("John", "Engineer");
        Contact comparisonContact = new ContactImpl(id, "John", "Engineer");
        Set comparisonSet = new HashSet();
        comparisonSet.add(comparisonContact);
        Set<Contact> contactsById = cm.getContacts(id);
        assertTrue(!contactsById.isEmpty());
        assertTrue(!comparisonSet.isEmpty());
        assertThat(contactsById, is(comparisonSet));
    }

    @Test 
    public void addMultipleContactsGeById(){
        int id = cm.addNewContact("John", "Engineer");
        Contact comparisonContact = new ContactImpl(id, "John", "Engineer");
        int id2 = cm.addNewContact("John", "Engineer");
        Contact comparisonContact2 = new ContactImpl(id2, "John", "Engineer");
        int id3 = cm.addNewContact("John", "Engineer");
        Contact comparisonContact3 = new ContactImpl(id3, "John", "Engineer");
        Set<Contact> contactsById = cm.getContacts(id, id2, id3);
        Set comparisonSet = new HashSet();
        comparisonSet.add(comparisonContact);
        comparisonSet.add(comparisonContact2);
        comparisonSet.add(comparisonContact3);
        assertTrue(!contactsById.isEmpty());
        assertTrue(!comparisonSet.isEmpty());
        assertThat(contactsById, is(comparisonSet));
    }

    @Test 
    public void addAContact(){
        int id = cm.addNewContact("John", "Engineer");
        Set<Contact> contactsByName = cm.getContacts("John");
        Set<Contact> contactsById = cm.getContacts(id);
        assertTrue(!contactsByName.isEmpty());
        assertTrue(!contactsById.isEmpty());
        assertThat(contactsByName, is(contactsById));
    }

    @Test 
    public void addMultipleContactsSameName(){
        int[] ids = addJohn3();
        Set<Contact> contactsByName = cm.getContacts("John");
        //TODO: add all relevant ids
        Set<Contact> contactsById = cm.getContacts(ids[0], ids[1], ids[2]);
        assertTrue(!contactsByName.isEmpty());
        assertTrue(!contactsById.isEmpty());
        assertThat(contactsByName, is(contactsById));
    }

    @Test 
    public void flushContacts(){
        int id = cm.addNewContact("John", "Engineer");
        int id2 = cm.addNewContact("John", "Engineer");
        int id3 = cm.addNewContact("John", "Engineer");
        cm.flush();
        ContactManager cm2 = new ContactManagerImpl();
        Set<Contact> cmContacts = cm.getContacts("John");
        Set<Contact> cm2Contacts = cm2.getContacts("John");
        assertThat(cmContacts, is(cm2Contacts));
    }
    @Test 
    public void flushContactsQuotesNewLineInName(){
        int id = cm.addNewContact("Joi\thn", "Engineer");
        int id2 = cm.addNewContact("Jo\"hn", "Engineer");
        int id3 = cm.addNewContact("Joh\nn", "Engineer");
        cm.flush();
        ContactManager cm2 = new ContactManagerImpl();
        Set<Contact> cmContacts = cm.getContacts(id, id2, id3);
        Set<Contact> cm2Contacts = cm2.getContacts(id, id2, id3);
        assertThat(cm2Contacts, is(cmContacts));
    }

    @Test(expected=NullPointerException.class)
    public void addFutureNullDate (){
        Set<Contact> cmContacts = new LinkedHashSet();
        cm.addFutureMeeting(cmContacts,null );
    }

    @Test(expected=IllegalArgumentException.class)
    public void addFutureMeetingPastDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 7, 12);
        Set<Contact> cmContacts = new LinkedHashSet();
        cm.addFutureMeeting(cmContacts,cal );
    }

    @Test(expected=IllegalArgumentException.class)
    public void getFutureMeetingWhichIsInThePast(){
        //Add past meeting, which we will search for using getFutureMeeting. This test relies on the curret dummy method creating a meeting with id 444
        Set<Contact> cntcts = new LinkedHashSet();
        Calendar cal = Calendar.getInstance();
        cm.addNewPastMeeting(cntcts, cal, "THIS THIS THIS");
        cm.getFutureMeeting(666);
    }
 
    @Test(expected=IllegalArgumentException.class)
    public void addFutureMeetingNonExistantContact(){
        Set<Contact> nonExistantContact  = new LinkedHashSet();
        Contact newContact = new ContactImpl(444, "Llewyn", "The protagonist of a movie I once watched");
        nonExistantContact.add(newContact);
        Locale locale1 = Locale.UK;
        TimeZone tz1 = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(tz1, locale1);
        cal.set(2016, 7, 12);
        cm.addFutureMeeting(nonExistantContact,cal );
    }

    @Test
    public void createRetrieveFututreMeeting(){
        Locale locale1 = Locale.UK;
        TimeZone tz1 = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(tz1, locale1);
        cal.set(2016, 7, 12);
        int id = cm.addNewContact("John", "Engineer");
        int id2 = cm.addNewContact("John", "Engineer");
        int id3 = cm.addNewContact("John", "Engineer");
        Set<Contact> cmContacts = cm.getContacts(id, id2, id3);
        int meetingId = cm.addFutureMeeting(cmContacts, cal);
        FutureMeeting retFut = cm.getFutureMeeting(meetingId);
        assertTrue(meetingId == retFut.getId());
        assertEquals(cal, retFut.getDate());
        assertEquals(cmContacts, retFut.getContacts());
    }

    @Test(expected=IllegalArgumentException.class)
    public void createPastMeetingEmptyContacts(){
        Set<Contact> cntcts = new LinkedHashSet();
        Calendar cal = Calendar.getInstance();
        cm.addNewPastMeeting(cntcts, cal,"TEST");
    }
    @Test(expected=IllegalArgumentException.class)
    public void createPastMeetingContactDoesntExist(){
        Calendar cal = Calendar.getInstance();
        int[] ids = addJohn3();
        Set<Contact> cntcts = cm.getContacts("John");
        Contact c = new ContactImpl(9999, "Maria", "Foge, comingo Maria.");
        cntcts.add(c);
        cm.addNewPastMeeting(cntcts, cal,"TEST");
    }
    @Test
    public void createRetrieveSortedPastMeetings(){
        Calendar calFirst = Calendar.getInstance();
        Calendar calMiddle = Calendar.getInstance();
        Calendar calMostRecent = Calendar.getInstance();
        int[] ids = addJohn3();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("dd/MM/yyyy");

        Set<Contact> cmContacts = cm.getContacts(ids[0], ids[1], ids[2]);
        calFirst.clear();
        calMiddle.clear();
        calMostRecent.clear();
        calFirst.set(2015,1,2);
        calMiddle.set(2015,6,7);
        calMostRecent.set(2015,10,1);
        cm.addNewPastMeeting(cmContacts, calFirst, "NOTES-First");
        cm.addNewPastMeeting(cmContacts, calMiddle, "NOTES-Middle");
        cm.addNewPastMeeting(cmContacts, calMostRecent, "NOTES-Recent");
        // create 3 past meetings - with different past dates ensure meetings are returned in correct orders
        Set<Contact> allToGet = cm.getContacts(ids[0]);
        Contact toGet = allToGet.iterator().next();
        List<PastMeeting> retPast = cm.getPastMeetingListFor(toGet);
        System.out.println("Number of meeting returned:" + retPast.size());
        PastMeeting retPastItemMostRecent = retPast.get(0);
        //retPast.iterator().remove();
        PastMeeting retPastItemMiddle = retPast.get(1);
        PastMeeting retPastItemFirst = retPast.get(2);
        System.out.println("Returning the same meeting always:" + (retPastItemMiddle == retPastItemFirst));
        System.out.println("Returning the same meeting always:" + (retPastItemMiddle == retPastItemMostRecent));
        System.out.println("Number of meeting available:" + retPast.size());
        assertTrue(retPast.size() ==3);
        for(PastMeeting pm : retPast){
            System.out.println("Sorted Return List-DOES IT WORK-->" + pm.getNotes() + "  ---  " + df.format(pm.getDate().getTime()) );
        }
        System.out.println("Most recent date:" +  df.format(retPastItemMostRecent.getDate().getTime()) );
        System.out.println("Recent  notes:" +  retPastItemMostRecent.getNotes()) ;
        System.out.println("Middle  notes:" +  retPastItemMiddle.getNotes()) ;
        System.out.println("Middle  date:" +  df.format(retPastItemMiddle.getDate().getTime()) );
        System.out.println("First date:" +  df.format(retPastItemFirst.getDate().getTime()) );
        System.out.println(df.format(calMostRecent.getTime()) + "----" + df.format(retPastItemMostRecent.getDate().getTime()) );
        assertEquals(calMostRecent, retPastItemMostRecent.getDate());
        assertEquals(cmContacts, retPastItemMostRecent.getContacts());
        assertEquals("NOTES-Recent", retPastItemMostRecent.getNotes());
        assertEquals(calMiddle, retPastItemMiddle.getDate());
        assertEquals(calFirst, retPastItemFirst.getDate());
    }

    @Test(expected=NullPointerException.class)
    public void getPastMtgForContactNull(){
        List<PastMeeting> pml = cm.getPastMeetingListFor(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getPastMtgForContactDoesntExist(){
        int[] ids = addJohn3();
        Contact breakIt = new ContactImpl(4444, "Stuart", "Adventurer");
        List<PastMeeting> pml = cm.getPastMeetingListFor(breakIt);
    }

}

