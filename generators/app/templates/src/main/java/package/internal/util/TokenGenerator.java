package <%= packageName %>.internal.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;

import static <%= packageName %>.internal.AppConstants.TOKEN_BYTE_LENGTH;


/**
 * Used as token generator for messaging and also for generating message tokens
 */
@Component("tokenGenerator")
public class TokenGenerator {

    private static final ObjectMapper mapper = createObjectMapper();

    public String generateHexToken() {

        return generateHexToken(TOKEN_BYTE_LENGTH);
    }

    public String generateBase64Token() {

        return generateBase64Token(TOKEN_BYTE_LENGTH);
    }

    //generateRandomHexToken(16) -> 2189df7475e96aa3982dbeab266497cd
    public String generateHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); //hex encoding
    }

    //generateRandomBase64Token(16) -> EEcCCAYuUcQk7IuzdaPzrg
    public String generateBase64Token(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token); //base64 encoding
    }

    public String md5Digest(final Object object) throws JsonProcessingException {
        return DigestUtils.md5DigestAsHex(mapper.writeValueAsBytes(object));
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

}
