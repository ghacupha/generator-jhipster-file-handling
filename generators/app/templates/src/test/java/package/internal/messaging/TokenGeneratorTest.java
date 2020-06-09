package <%= packageName %>.internal.messaging;

import <%= packageName %>.internal.messaging.sample.Greetings;
import <%= packageName %>.internal.util.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TokenGeneratorTest {

    private TokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        tokenGenerator = new TokenGenerator();
    }

    @Test
    void testStabilityOfHashFunction() throws Exception {

        String message = "There must always be a Stark in Winterfell";
        String description = "About the Starks";
        long timestamp = System.currentTimeMillis();

        Greetings greeting = Greetings.builder().message(message).description(description).timestamp(timestamp).build();
        String messageTokenValue = tokenGenerator.md5Digest(greeting);

        assertThat(tokenGenerator.md5Digest(greeting)).isEqualTo(messageTokenValue);

        greeting.setMessageToken(messageTokenValue);

        assertThat(tokenGenerator.md5Digest(greeting)).isNotEqualTo(messageTokenValue);
    }
}
