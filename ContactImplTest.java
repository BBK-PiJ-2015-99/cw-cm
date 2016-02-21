import org.junit.*;
import static org.junit.Assert.*;

public class ContactImplTest {

    Contact contact;
    Contact contactSimple;

    @Before
    public void initTestContactImpl(){
        contact = new ContactImpl(1, "Gene Kranz", "Flight Director");
        contactSimple = new ContactImpl(2, "Christ Kraft");
    }

    @Test
    public void getId(){
        System.out.println("THIS THIS THIS" + contact.getId());
        assertSame(Integer.valueOf(1), contact.getId());
        assertSame(Integer.valueOf(2), contactSimple.getId());
    }
}
