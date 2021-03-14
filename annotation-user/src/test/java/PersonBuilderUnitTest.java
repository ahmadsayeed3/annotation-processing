import com.baeldung.annotation.Person;
import com.baeldung.annotation.PersonBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonBuilderUnitTest {

    @Test
    public void whenBuildPersonWithBuilder_thenObjectHasPropertyValues() {

        Person person = new PersonBuilder().setAge(25).setName("John").build();
        System.out.println("Yahooooo: " + person.getAge());
        assertEquals(25, person.getAge());
        assertEquals("John", person.getName());

    }

}
