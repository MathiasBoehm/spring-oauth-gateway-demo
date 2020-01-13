package de.struktuhr.springgatewaydemo;

        import org.junit.Test;

        import java.security.*;
        import java.security.spec.PKCS8EncodedKeySpec;
        import java.util.Base64;

public class KeyGenerator {

    @Test
    public void generateKeys() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair pair = kpg.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(publicKey.getEncoded());

        byte[] privateKeyString = makeWritable(privateKey);
        byte[] publicKeyString = makeWritable(encoded.getEncoded());

        System.out.println("Private");
        System.out.println(new String(privateKeyString));

        System.out.println("Public");
        System.out.println(new String(publicKeyString));
    }

    private byte[] makeWritable(Key key) {
        byte[] encoded = key.getEncoded();
        return makeWritable(encoded);
    }

    private byte[] makeWritable(byte[] content) {
        return Base64.getEncoder().encode(content);
    }
}
