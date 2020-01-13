package de.struktuhr.springgatewaydemo.token;


import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;

@Component
public class TokenService {

    private final static Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${app.publickey}")
    private String publicKeyString;

    public SignedJWT parseJWT(String signedJwtTokenString) {
        try {
            return SignedJWT.parse(signedJwtTokenString);
        }
        catch (ParseException e) {
            logger.error("Cannot parse token '{}'. {}", signedJwtTokenString, e.getMessage());
            throw new InvalidTokenException(e);
        }
    }

    public boolean verify(SignedJWT signedJWT)  {
        try {
            RSAPublicKey publicKey = readPublicKey();
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            return signedJWT.verify(verifier);
        }
        catch (Exception e) {
            throw new InvalidTokenException(e);
        }
    }

    private RSAPublicKey readPublicKey() throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyString);
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decodedKey));
    }
}
