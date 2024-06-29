import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;

public class TokenTest {
    @Autowired
    private JwtTokenService jwtTokenService;
    @Test
    public void testToken() {
        assert(jwtTokenService.parseToken("Bearer {token{{}}abcdef}").equals("token{{}}abcdef"));
    }
}
