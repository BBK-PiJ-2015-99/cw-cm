import org.junit.*;
import static org.junit.Assert.*;
import java.util.Set;
import static org.hamcrest.CoreMatchers.*;

public class ContactManagerImplTest {


    private ContactManager cm;

    @Before 
    public void initTest(){
        cm = new ContactManagerImpl();
    }

    @Test 
    public void addAContact(){
        int id = cm.addNewContact("John", "Engineer");
        Set<Contact> contactsByName = cm.getContacts("John");
        Set<Contact> contactsById = cm.getContacts(id);
        assertTrue(!contactsByName.isEmpty());
        assertTrue(!contactsById.isEmpty());
        //assertThat(contactsByName, is(contactsById)); 
    }

    @Test 
    public void addContactSameName(){
        int id = cm.addNewContact("John", "Engineer");
        id = cm.addNewContact("John", "Engineer");
        id = cm.addNewContact("John", "Engineer");
        Set<Contact> contactsByName = cm.getContacts("John");
        Set<Contact> contactsById = cm.getContacts(id);
        System.out.println("----------->");
        System.out.println(contactsByName.isEmpty());
        System.out.println(contactsById.isEmpty());
        assertTrue(!contactsByName.isEmpty());
        assertTrue(!contactsById.isEmpty());
    }

}
