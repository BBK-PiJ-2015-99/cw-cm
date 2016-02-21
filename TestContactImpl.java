import org.junit.*;
import static org.junit.Assert.*;

public class TestContactImpl {

    Contact contact;
    Contact contactSimple;

    @BeforeClass
    public void initTestContactImpl(){
        contact = new ContactImpl(1, "Gene Kranz", "Flight Director");
        contactSimple = new ContactImpl(2, "Christ Kraft");
    }

    public void getId(){
        assertSame(Integer.valueOf(1), contact.getId());
        assertSame(Integer.valueOf(2), contact.getId());
    }
}
