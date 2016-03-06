import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;

public class ContactImplTest {

    Contact contact;
    Contact contactSimple;

    @Before
    public void initTestContactImpl(){
        contact = new ContactImpl(1, "Gene Kranz", "Flight Director");
        contactSimple = new ContactImpl(2, "Chris Kraft");
    }

    @Test
    public void getId(){
        assertSame(Integer.valueOf(1), contact.getId());
        assertSame(Integer.valueOf(2), contactSimple.getId());
    }

    @Test
    public void getName(){
        assertEquals("Gene Kranz", contact.getName());
        assertEquals("Chris Kraft", contactSimple.getName());
    }


    @Test
    public void getNotes(){
        assertEquals("Flight Director", contact.getNotes());
        assertEquals("", contactSimple.getNotes());
    }

    @Test
    public void addNotes(){
        contact.addNotes("For Apollo 13 Mission");
        assertEquals("Flight DirectorFor Apollo 13 Mission", contact.getNotes());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void negativeId(){
       Contact c = new ContactImpl(-1,"TEST","Test"); 
    }

    @Test(expected=NullPointerException.class)
    public void nullName(){
        Contact c = new ContactImpl(1,null,"test");
    }
    @Test(expected=NullPointerException.class)
    public void nullNote(){
        Contact c = new ContactImpl(1,"test",null);
    }
    @Test(expected=NullPointerException.class)
    public void nullNoteAndName(){
        Contact c = new ContactImpl(1,null,null);
    }
    @Test
    public void objDiffId(){
        Contact c1 = new ContactImpl(4,"Spielberg", "A director");
        Contact c2 = new ContactImpl(6,"Spielberg", "A director");
        assertThat(c1, not(c2));
    }
    
    @Test
    public void objDiffName(){
        Contact c1 = new ContactImpl(4,"Spielderg", "A director");
        Contact c2 = new ContactImpl(6,"Spielberg", "A director");
        assertThat(c1, not(c2));
    }
    @Test
    public void sameObjectsEqual(){
        Contact c1 = new ContactImpl(4,"Spielberg", "A director");
        Contact c2 = new ContactImpl(4,"Spielberg", "A director");
        assertEquals(c1, c2);
        //assertThat(c1, is(c2));
    }
    @Test
    public void equalSets(){
        Contact c1 = new ContactImpl(4,"Spielberg", "A director");
        Contact c2 = new ContactImpl(5,"Underwood", "A politician");

        Contact c3 = new ContactImpl(4,"Spielberg", "A director");
        Contact c4 = new ContactImpl(5,"Underwood", "A politician");

        Set<Contact> s1 = new LinkedHashSet();
        Set<Contact> s2 = new LinkedHashSet();
        s1.add(c1);
        s1.add(c2);
    
        s2.add(c3);
        s2.add(c4);
        assertEquals(s1, s2);
    }
    @Test
    public void equalLists(){
        Contact c1 = new ContactImpl(4,"Spielberg", "A director");
        Contact c2 = new ContactImpl(5,"Underwood", "A politician");

        Contact c3 = new ContactImpl(4,"Spielberg", "A director");
        Contact c4 = new ContactImpl(5,"Underwood", "A politician");

        List l1 = new LinkedList();
        List l2 = new LinkedList();
        l1.add(c1);
        l1.add(c2);
    
        l2.add(c3);
        l2.add(c4);

        assertEquals(l1, l2);
    }
}
