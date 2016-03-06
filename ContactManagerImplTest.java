

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
        int id = cm.addNewContact("John", "Engineer");
        int id2 = cm.addNewContact("John", "Engineer");
        int id3 = cm.addNewContact("John", "Engineer");
        Set<Contact> contactsByName = cm.getContacts("John");
        //TODO: add all relevant ids
        Set<Contact> contactsById = cm.getContacts(id, id2, id3);
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
    @Test
    public void createMeeting(){
        Locale locale1 = Locale.UK;
        TimeZone tz1 = TimeZone.getTimeZone("GMT");
        int id = cm.addNewContact("John", "Engineer");
        int id2 = cm.addNewContact("John", "Engineer");
        int id3 = cm.addNewContact("John", "Engineer");
        Set<Contact> cmContacts = cm.getContacts(id, id2, id3);
        Calendar cal = Calendar.getInstance(tz1, locale1);
        cal.set(2016, 7, 12);
        //int meetingId = cm.addFutureMeeting(cmContacts, cal);
        //retrievedFutureMeeting = cm.getFutureMeeting(meetingId);
        assertTrue(true);
    }
    

    
}
