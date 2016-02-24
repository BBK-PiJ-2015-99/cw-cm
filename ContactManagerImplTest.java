import org.junit.*;
import static org.junit.Assert.*;
import java.util.Set;


public class ContactManagerImpl {


    private ContactManager cm;

    @Before 
    public void initTest(){
        ContactManager cm = new ContactManager();
    }

    @Test void addAContact(){
        int id = cm.addNewContact("John", "Engineer");
        Set<Contact> contactsByName = cm.getContacts("John");
        Set<Contact> contactsById = cm.getContacts(id);
        assertEquals(Integer.valueOf(1),contactsByName.size()); 
        assertEquals(Integer.valueOf(1),contactsById.size()); 
    }

}

}
