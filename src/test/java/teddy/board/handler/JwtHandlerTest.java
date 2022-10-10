package teddy.board.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtHandlerTest {
    JwtHandler jwtHandler = new JwtHandler();

    @Test
    void createTokenTest() {
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = jwtHandler.createToken(encodedKey, "subject", 60L);

        Assertions.assertThat(token).contains("Bearer ");
    }

    @Test
    void extractSubjectTest() {
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String subject = "subject";
        String token = createToken(encodedKey, subject, 60L);

        String extractedSubject = jwtHandler.extractSubject(encodedKey, token);

        Assertions.assertThat(extractedSubject).isEqualTo(subject);
    }

    private String createToken(String encodedKey, String subject, long maxAgeSeconds) {
        return jwtHandler.createToken(encodedKey, subject, maxAgeSeconds);
    }
}