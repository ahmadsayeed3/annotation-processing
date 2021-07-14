

import com.sid.learn.Person;
import com.sid.learn.PersonBuilder;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonBuilderUnitTest {

    @Test
    public void whenBuildPersonWithBuilder_thenObjectHasPropertyValues() {

        Person person = new PersonBuilder().setAge(25).setName("John").build();
        System.out.println("Yahooooo: " + person.getAge());
        assertEquals(25, person.getAge());
        assertEquals("John", person.getName());

    }

}
