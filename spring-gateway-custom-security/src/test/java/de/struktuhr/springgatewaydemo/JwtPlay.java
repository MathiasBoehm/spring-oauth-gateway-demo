package de.struktuhr.springgatewaydemo;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Test;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import static com.nimbusds.jose.JOSEObjectType.JWT;
import static com.nimbusds.jose.JWSAlgorithm.RS256;

public class JwtPlay {

    final static String PRIVATE_KEY_STRING = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8iGFxqd6uX4OPWuaHWxw7L7Xi1xG/BYXV6XdllQ2CQgu7e8fW0jJr5SQVs31n0h1x6MeCE476q5X3KOCszBju0Vlb3l6zp2yU+5WuCh8iUrQEZMPc/H4X85Z4o6/IEPqoRxO5F6/ytYJTmj8gkqXgfyYzWdqzn6zCqjepjCjjhKgE3wD/0gblvC7Fv2D9N4oS41aaymP29RmqHUgwUd4jl1i93EhPyH4jEsB1DGAfn5eqGrMKHXPVCd5y6qHa6c4gu/aluxwOjbsJsDz7se4Prvp/D7CFHoRFEiwoYSvrM3YEoo4omg/Nlt17fLUJFDVCVLXaLUdlHYaO/3mgDJu3AgMBAAECggEAEY0MrT0oGsmPgMfwkAc+UJA3eX+WCldkRy6ty8odcNPfLbvvNaNpyDuTqWWFNiYMUa9e61AQzmss0c5/Y/a4F3truy6vWEAszo3BOGt+zH4wffw3c5oZzvcBOgfa9GAIRaQgROncmlalZE0GtWyRfp23m64wuuoRdinMdr8yi0OETQQM/y011NUEPCfoaH5fyfcVnYJcffRwhMw4ZUVSxwc8oiqTjAttmIU9TTSJUZ6IDNnH1SlB1Tfr3ieaFsls/pJrLSCiwRYls7i1TXzXod98+s8oqo/SauZiwgJdcgq6zg/42g7ZtC9N/xF7x1Y+gLfYtTbhHz/IZ6ll01tfuQKBgQDdY24MzNEtT8p7eCeUSh0xp/KdaO3+BonsqXg5drzqLoVZAtwV5A6l83zDC0gTyHnCxzO6HcsIMUqJKfHy6y9VHfAqKBlBI2/M8cf4ImVeuupoEQ25bx0T6Se6lkFzkvnZVQ6qT+SChojwOI5jqgXeDMuqbTW4teV5wFh5FRLgjQKBgQDaAfop5h+Qh++Ov5fcH8oQO1IWyIqeEZy2CPa/BYq2bb9kq4zxpmM2IyFuekOVeIM1Cxt6X4qQSwL4Nr2tSJ7p9957UQlK1po5th9codJITeGdXm9AfH6GBbIQNMyJH0cLuLwB+Qf40gbT8Zd3ZRjr4Eyal317CqeNm1fYL/SGUwKBgGfGM8/MMkELJd66c9MO6qBcGV2v0d9BMWW+CpzlC6i6JceTj5azUCp5+AF1SFP9pRWoSpbzYDh24nlpMACuPYGohus74JqoAcAPlay6csn00+v0EnrZmse6guTzibqjKnq1ZCpxSKvmZEo2OIh/KkDrvowkpQnwOaDYiKA5QKABAoGAZW5eEnh7XyegEe6anVQ9E2hGPUt5Eypmm1t4UlAlxdMyJYibvyMHLf0SZPCF2VmE0n0kf79MHO6Jpyrw/Vq4dWtaHE4L4MIQZ6GQ3ex8hh+WRT3WzU4fIAQuYNPwBiyDRbTh5JkVWHN/scTKEEb9XWGywoqRW1oNsxj/uho1zvkCgYEAknpeNl1tFigebFgVjRmKiAR9r2eZE0WQW6vTq+zAztoKmvW2CDU4TpbUC7f/SuUkjHSnYaab4UsgTpJ2efdviOGeqPBN+t5lL/gIgG0vJ8VlTf6H+m2A2/X/YnhcxIC5tDGxMOsSXhqnesNcodtMesj2V0/b2yTQtrUIM8kcNik=";

    final static String PUBLIC_KEY_STRING = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvIhhcanerl+Dj1rmh1scOy+14tcRvwWF1el3ZZUNgkILu3vH1tIya+UkFbN9Z9IdcejHghOO+quV9yjgrMwY7tFZW95es6dslPuVrgofIlK0BGTD3Px+F/OWeKOvyBD6qEcTuRev8rWCU5o/IJKl4H8mM1nas5+swqo3qYwo44SoBN8A/9IG5bwuxb9g/TeKEuNWmspj9vUZqh1IMFHeI5dYvdxIT8h+IxLAdQxgH5+XqhqzCh1z1Qnecuqh2unOILv2pbscDo27CbA8+7HuD676fw+whR6ERRIsKGEr6zN2BKKOKJoPzZbde3y1CRQ1QlS12i1HZR2Gjv95oAybtwIDAQAB";

    @Test
    public void checkJwt() throws Exception {
        JWSHeader header = new JWSHeader.Builder(RS256)
                .keyID("4711").
                        type(JWT).
                        build();


        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .subject("user2")
                .issuer("https://demo.com")
                .claim("customer", "1234567890")
                .claim("usertype", "employee")
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000))
                .build();

        // Sign
        SignedJWT signedJWT = new SignedJWT(header, claimSet);
        PrivateKey privateKey = readPrivateKey(PRIVATE_KEY_STRING);
        RSASSASigner signer = new RSASSASigner(privateKey);
        signedJWT.sign(signer);

        String signedJwtTokenString = signedJWT.serialize();
        System.out.println("signedJwtTokenString = " + signedJwtTokenString);

        // Verify
        RSAPublicKey publicKey = readPublicKey(PUBLIC_KEY_STRING);
        signedJWT = SignedJWT.parse(signedJwtTokenString);

        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        boolean verified = signedJWT.verify(verifier);
        System.out.println("Verified = " + verified);
    }


    public RSAPublicKey readPublicKey(String publicKeyString) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyString);
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decodedKey));
    }

    private PrivateKey readPrivateKey(String privateKeyString) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyString);
        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }

}
