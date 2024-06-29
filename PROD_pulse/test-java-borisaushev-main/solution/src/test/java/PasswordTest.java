import org.junit.jupiter.api.Test;
import static ru.prodcontest.userInfo.UserDataUtil.*;

public class PasswordTest {
    @Test
    public void testPassword() {
        System.out.println(hashPassword("$PASSw0rd^") + " : " + hashPassword("$PASSw0rd^"));
        assert(hashPassword("$PASSw0rd^").equals(hashPassword("$PASSw0rd^")));
    }
}
