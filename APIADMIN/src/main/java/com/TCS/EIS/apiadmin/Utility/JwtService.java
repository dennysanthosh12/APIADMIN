package com.TCS.EIS.apiadmin.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	
	@Value("${jwt.keystore.location}")
    private Resource keystore;

    @Value("${jwt.keystore.password}")
    private String keystorePassword;

    @Value("${jwt.keystore.alias}")
    private String keyAlias;

    @Value("${jwt.keystore.key-password}")
    private String keyPassword;

    @Value("${jwt.expiration-ms}")
    private long jwtExpiration;

    private PrivateKey privateKey;
    private PublicKey publicKey;
	
	
    
    @PostConstruct
    public void loadKeys() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keystore.getInputStream(), keystorePassword.toCharArray());

            this.privateKey = (PrivateKey) keyStore.getKey(
                    keyAlias,
                    keyPassword.toCharArray()
            );

            Certificate certificate = keyStore.getCertificate(keyAlias);
            this.publicKey = certificate.getPublicKey();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load keys from keystore", e);
        }
    }


	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        // Inject roles into the token claims
        extraClaims.put("roles", userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList());
        
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(privateKey)// 24 hours
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
    	return Jwts.parser() // Changed from parserBuilder()
                .verifyWith(publicKey) // Use verifyWith() instead of setSigningKey()
                .build()
                .parseSignedClaims(token) // Use parseSignedClaims() instead of parseClaimsJws()
                .getPayload(); 
    }

    

}
