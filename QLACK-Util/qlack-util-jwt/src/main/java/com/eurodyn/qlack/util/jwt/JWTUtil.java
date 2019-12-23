package com.eurodyn.qlack.util.jwt;

import com.eurodyn.qlack.util.jwt.dto.JWTClaimsRequestDTO;
import com.eurodyn.qlack.util.jwt.dto.JWTClaimsResponseDTO;
import com.eurodyn.qlack.util.jwt.dto.JWTGenerateRequestDTO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

/**
 * A utility class to generate and validate JSON Web Tokens.
 */
public class JWTUtil {

  public static final String TOKEN_PREFIX = "Bearer ";

  private JWTUtil() {
  }

  /**
   * Creates a new JWS based on the parameters specified.
   *
   * @param request The parameters to be used to create the JWT.
   */
  public static String generateToken(JWTGenerateRequestDTO request) {

    // The JWT signature algorithm to be used to sign the token.
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // Set current time.
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    // We will sign our JWT with our ApiKey secret
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(
      Base64.encodeBase64String(request.getSecret().getBytes()));
    Key signingKey = new SecretKeySpec(apiKeySecretBytes,
      signatureAlgorithm.getJcaName());

    // Set the JWT claims.
    JwtBuilder builder = Jwts.builder()
      .setId(request.getId())
      .setIssuedAt(now)
      .setSubject(request.getSubject())
      .setIssuer(request.getIssuer())
      .signWith(signatureAlgorithm, signingKey);

    // If it has been specified, add the expiration for the token.
    if (request.getTtl() >= 0) {
      long expMillis = nowMillis + request.getTtl();
      Date exp = new Date(expMillis);
      builder.setExpiration(exp);
    }

    // Add additional claims if any.
    if (request.getClaims() != null) {
      builder.addClaims(request.getClaims());
    }

    // Builds the JWT and serializes it to a compact, URL-safe string with the prefix.
    return TOKEN_PREFIX + builder.compact();
  }

  /**
   * Returns the claims found in a JWT while it is also verifying the token.
   *
   * @param request The JWT to be verified together with the secret used to
   * sign it.
   */
  public static JWTClaimsResponseDTO getClaims(JWTClaimsRequestDTO request) {
    JWTClaimsResponseDTO response = new JWTClaimsResponseDTO();

    try {
      response.setClaims(
        Jwts.parser()
          .setSigningKey(
            Base64.encodeBase64String(request.getSecret().getBytes()))
          .setAllowedClockSkewSeconds(request.getAllowedTimeSkew())
          .parseClaimsJws(request.getJwt()).getBody());
      response.setValid(true);
    } catch (Exception e) {
      response.setValid(false);
      response.setErrorMessage(e.getMessage());
    }

    return response;
  }

  public static Object getSubject(JWTClaimsRequestDTO request) {
    String jwt = request.getJwt().replace(TOKEN_PREFIX, "");

    return Jwts.parser()
      .setSigningKey(Base64.encodeBase64String(request.getSecret().getBytes()))
      .setAllowedClockSkewSeconds(request.getAllowedTimeSkew())
      .parseClaimsJws(jwt)
      .getBody()
      .getSubject();
  }

  /**
   * Returns the value of a specific claim in JWT while also verifying the
   * JWT.
   *
   * @param jwtClaimsRequest The JWT to be verified together with the secret
   * used to sign it.
   * @param claim The name of the claim to return.
   * @return The calue of the requested claim.
   */
  public static Object getClaimValue(JWTClaimsRequestDTO jwtClaimsRequest,
    String claim) {
    JWTClaimsResponseDTO claims = getClaims(jwtClaimsRequest);

    if (claims != null && claims.getClaims() != null && claims.getClaims()
      .containsKey(claim)) {
      return claims.getClaims().get(claim);
    } else {
      return null;
    }
  }

  /**
   * Parses a JWT token (compact or normal) and returns its String
   * representation while it is also validating the token.
   *
   * @param jwt The JWT to decode.
   * @param secret The secret used to sign the JWT.
   * @return Returns the String representation of the JWT.
   */
  public static String tokenToString(String jwt, String secret) {
    return Jwts.parser()
      .setSigningKey(Base64.encodeBase64String(secret.getBytes()))
      .parse(jwt)
      .toString();
  }

  public static String getRawToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    return token != null ? token.replace(TOKEN_PREFIX, "") : null;
  }
}
