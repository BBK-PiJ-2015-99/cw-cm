import org.junit.*;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;
import static org.hamcrest.CoreMatchers.*;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;

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
        System.out.println("Size of old set:" + cmContacts.size());
        Set<Contact> cm2Contacts = cm2.getContacts("John");
        System.out.println("Size of enew one after loading:" + cm2Contacts.size());
        System.out.println(" ARE THE TWO SETS EQUALS");
        System.out.println(cmContacts.equals(cm2Contacts));
        System.out.println(cm2Contacts.equals(cmContacts));
        System.out.println(cmContacts.contains(cm2Contacts));
        for(Contact c: cmContacts){
            System.out.println(c.getId() + "--" + c.getName() + "--" + c.getNotes()  );
        }

        for(Contact c: cm2Contacts){
            System.out.println(c.getId() + "--" + c.getName() + "--" + c.getNotes()  );
        }
        assertTrue(cmContacts.contains(cm2Contacts));
    }
}
