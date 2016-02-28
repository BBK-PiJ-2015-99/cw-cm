import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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

    @Test(expected=IllegalArgumentException.class)
    public void nullId(){
       Contact c = new ContactImpl(null,"TEST","Test"); 
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
    public void objDiffComment(){
        Contact c1 = new ContactImpl(4,"Spielberg", "A director");
        Contact c2 = new ContactImpl(6,"Spielberg", "A birector");
        assertThat(c1, not(c2));
    }
    @Test
    public void sameObjectsEqual(){
        Contact c1 = new ContactImpl(4,"Spielberg", "A director");
        Contact c2 = new ContactImpl(4,"Spielberg", "A director");
        assertThat(c1, is(c2));
    }
}
