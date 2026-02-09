package at.technikum.backend;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Deaktiviert, weil CI/Local keine DB startet. Unit-Tests sollen ohne DB laufen.")
@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
