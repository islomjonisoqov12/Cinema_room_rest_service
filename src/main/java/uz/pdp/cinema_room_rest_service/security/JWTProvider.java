package uz.pdp.spring_boot_exam_b6_islomjon.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component

public class JWTProvider {

    long expirationTime = 1000 * 3600;
    String secretKey = "islomSecretkey";

    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return token;
    }

    public boolean validateToken(String tokenClient, HttpServletRequest httpServletRequest) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(tokenClient);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Muddati o'tgan");
            httpServletRequest.setAttribute("expired", e.getMessage());
        } catch (MalformedJwtException malformedJwtException) {
            System.err.println("Buzilgan token");
        } catch (SignatureException s) {
            System.err.println("Kalit so'z xato");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            System.err.println("Qo'llanilmagan token");
        } catch (IllegalArgumentException ex) {
            System.err.println("Bo'sh token");
        }
        return false;

    }

    public String getUsernameFromToken(String tokenClient) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(tokenClient)
                .getBody()
                .getSubject();

    }
}
