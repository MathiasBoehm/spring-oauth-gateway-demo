package de.struktuhr.springgatewaydemo.token;

import com.nimbusds.jwt.SignedJWT;

public class CustomTokenHelper {

    public static String buildUserInfo(SignedJWT jwt) {
        try {
            String userId = jwt.getJWTClaimsSet().getSubject();
            String customer = jwt.getJWTClaimsSet().getStringClaim("customer");
            String usertype = jwt.getJWTClaimsSet().getStringClaim("usertype");
            StringBuilder sb = new StringBuilder(userId);
            if (usertype != null) {
                sb.append(";").append(usertype);
            }
            if (customer != null) {
                sb.append(";").append(customer);
            }
            return sb.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
