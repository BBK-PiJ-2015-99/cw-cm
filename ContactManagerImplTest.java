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
   
    private void addMeetingsJohn3(Calendar calFirst, Calendar calMiddle, Calendar calMostRecent, boolean isPastMeeting){
        int[] ids = addJohn3();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("dd/MM/yyyy");
        Set<Contact> cmContacts = cm.getContacts(ids[0], ids[1], ids[2]);
        /*calFirst.clear();
        calMiddle.clear();
        calMostRecent.clear();*/
        Set<Contact> allToGet = cm.getContacts(ids[0]);
        Contact toGet = allToGet.iterator().next();
        if(isPastMeeting){
            cm.addNewPastMeeting(cmContacts, calFirst, "NOTES-First");
            cm.addNewPastMeeting(cmContacts, calMiddle, "NOTES-Middle");
            cm.addNewPastMeeting(cmContacts, calMostRecent, "NOTES-Recent");
        } else {
            cm.addFutureMeeting(cmContacts, calFirst);
            cm.addFutureMeeting(cmContacts, calMiddle);
            cm.addFutureMeeting(cmContacts, calMostRecent);
        }    
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
    public void createRetrieveSortedPastMeetingsFor(){
        Calendar calFirst = Calendar.getInstance();
        Calendar calMiddle = Calendar.getInstance();
        Calendar calMostRecent = Calendar.getInstance();
        calFirst.set(2015,1,2);
        calMiddle.set(2015,6,7);
        calMostRecent.set(2015,10,1);
        addMeetingsJohn3(calFirst, calMiddle, calMostRecent, true);
        Set<Contact> allToGet = cm.getContacts("John");
        Contact toGet = allToGet.iterator().next();
        List<PastMeeting> retPast = cm.getPastMeetingListFor(toGet);
        PastMeeting retPastItemMostRecent = retPast.get(0);
        PastMeeting retPastItemMiddle = retPast.get(1);
        PastMeeting retPastItemFirst = retPast.get(2);
        assertTrue(retPast.size() ==3);
        assertEquals(calMostRecent, retPastItemMostRecent.getDate());
        assertEquals(allToGet, retPastItemMostRecent.getContacts());
        assertEquals("NOTES-Recent", retPastItemMostRecent.getNotes());
        assertEquals(calMiddle, retPastItemMiddle.getDate());
        assertEquals(calFirst, retPastItemFirst.getDate());
    }

    @Test
    public void createRetrieveSortedFutureMeetingsFor(){
        Calendar calSoonest = Calendar.getInstance();
        Calendar calMiddle = Calendar.getInstance();
        Calendar calLast = Calendar.getInstance();
        calSoonest.set(2017,1,2);
        calMiddle.set(2017,6,7);
        calLast.set(2017,10,1);
        addMeetingsJohn3(calSoonest, calMiddle, calLast, false);
        Set<Contact> allToGet = cm.getContacts("John");
        Contact toGet = allToGet.iterator().next();
        List<Meeting> retPast = cm.getFutureMeetingList(toGet);
        Meeting retPastItemSoonest = retPast.get(0);
        Meeting retPastItemMiddle = retPast.get(1);
        Meeting retPastItemLast = retPast.get(2);
        assertTrue(retPast.size() ==3);
        assertEquals(calSoonest, retPastItemSoonest.getDate());
        assertEquals(allToGet, retPastItemSoonest.getContacts());
        assertEquals(calMiddle, retPastItemMiddle.getDate());
        assertEquals(calLast, retPastItemLast.getDate());
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

    @Test(expected=NullPointerException.class)
    public void getFutureMtgForContactNull(){
        List<Meeting> pml = cm.getFutureMeetingList(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFutureMtgForContactDoesntExist(){
        int[] ids = addJohn3();
        Contact breakIt = new ContactImpl(4444, "Stuart", "Adventurer");
        List<Meeting> pml = cm.getFutureMeetingList(breakIt);
    }

    @Test(expected=IllegalArgumentException.class)
    public void getFutureMeetingByPastMeetingExists(){
        int[] ids = addJohn3();
        Calendar calSoonest = Calendar.getInstance();
        calSoonest.set(2017,1,2);
        Set<Contact> cntc = cm.getContacts("John");
        Set<Contact> cntSet = new HashSet();
        System.out.println("Number of elements in set:" + cntSet.size());
        cntSet.add(cntc.iterator().next());
        ContactManagerImpl cm2 = (ContactManagerImpl) cm;
        cm2.injectMeetingForTest(1, cntSet, calSoonest,  true);
        FutureMeeting fm = cm.getFutureMeeting(1); 
    }
    
    public void getFutureMeetingById(){
        Calendar calSoonest = Calendar.getInstance();
        Calendar calMiddle = Calendar.getInstance();
        Calendar calLast = Calendar.getInstance();
        calSoonest.set(2017,1,2);
        calMiddle.set(2017,6,7);
        calLast.set(2017,10,1);
        addMeetingsJohn3(calSoonest, calMiddle ,calLast, false);
        FutureMeeting fm = cm.getFutureMeeting(1); 
        FutureMeeting fm2 = cm.getFutureMeeting(2); 
        FutureMeeting fm3 = cm.getFutureMeeting(3); 
        assertEquals(calSoonest, fm.getDate());
        assertEquals(calMiddle, fm2.getDate());
        assertEquals(calLast, fm3.getDate());
    }

    @Test(expected=IllegalArgumentException.class)
    public void getPastMeetingByFutureMeetingExists(){
        int[] ids = addJohn3();
        Calendar calSoonest = Calendar.getInstance();
        calSoonest.set(2017,1,2);
        Set<Contact> cntc = cm.getContacts("John");
        Set<Contact> cntSet = new HashSet();
        System.out.println("Number of elements in set:" + cntSet.size());
        cntSet.add(cntc.iterator().next());
        ContactManagerImpl cm2 = (ContactManagerImpl) cm;
        cm2.injectMeetingForTest(1, cntSet, calSoonest,  false);
        PastMeeting fm = cm.getPastMeeting(1); 
    }

    @Test
    public void getPastMeetingById(){
        Calendar calSoonest = Calendar.getInstance();
        Calendar calMiddle = Calendar.getInstance();
        Calendar calLast = Calendar.getInstance();
        calSoonest.set(2015,1,2);
        calMiddle.set(2015,6,7);
        calLast.set(2015,10,1);
        addMeetingsJohn3(calSoonest, calMiddle ,calLast, true);
        PastMeeting fm = cm.getPastMeeting(1); 
        PastMeeting fm2 = cm.getPastMeeting(2); 
        PastMeeting fm3 = cm.getPastMeeting(3); 
        System.out.println("WHAT WAS RETURNED???");
        System.out.println(fm);
        assertEquals(calSoonest, fm.getDate());
        assertEquals(calMiddle, fm2.getDate());
        assertEquals(calLast, fm3.getDate());
    }
}

