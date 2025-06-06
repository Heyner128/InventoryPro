package me.heyner.stashless.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private Long jwtExpiration;

  public Long getJwtExpiration() {
    return jwtExpiration;
  }

  private SecretKey getSignInKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
    return buildToken(claims, userDetails, Clock.systemUTC(), jwtExpiration);
  }

  public long getExpirationTime() {
    return jwtExpiration;
  }

  public String buildToken(
      Map<String, Object> claims, UserDetails userDetails, Clock clock, long expirationDelta) {

    long currentMillis = Instant.now(clock).toEpochMilli();

    return Jwts.builder()
        .claims(claims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(currentMillis))
        .expiration(new Date(currentMillis + expirationDelta))
        .signWith(getSignInKey())
        .compact();
  }
}
