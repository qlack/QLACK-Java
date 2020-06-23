package com.eurodyn.qlack.util.jwt.service;

import com.eurodyn.qlack.util.jwt.config.AppConstants;
import com.eurodyn.qlack.util.jwt.config.AppPropertiesUtilJwt;
import com.eurodyn.qlack.util.jwt.dto.JwtClaimsDTO;
import com.eurodyn.qlack.util.jwt.dto.JwtGenerateRequestDTO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * A service allowing creation, validation, and claims management in JWTs.
 */
@Service
public class JwtService {

  private final AppPropertiesUtilJwt appProperties;

  public JwtService(AppPropertiesUtilJwt appProperties) {
    this.appProperties = appProperties;
  }

  /**
   * Generates a JWT encapsulating the requested paramaters including any number of custom claims.
   *
   * @param request The details of the JWT to be generated.
   * @return Returns a compacted JWT encapsulating the specified details as per <a
   * href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-7">JWT Compact
   * Serialization</a>
   */
  public String generateJwt(JwtGenerateRequestDTO request) {
    // The JWT signature algorithm to be used to sign the token.
    final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // Set current time.
    final Instant now = Instant.now();

    // Sign JWT with the ApiKey secret
    final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(
        Base64.encodeBase64String(appProperties.getJwtSecret().getBytes()));
    final Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    // Prepare JWT properties..
    final JwtBuilder builder = Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date.from(now))
        .setNotBefore(Date.from(now))
        .setSubject(request.getSubject())
        .setIssuer(appProperties.getJwtIssuer())
        .setExpiration(new Date(Instant.now().plus(appProperties.getJwtTtlMinutes(),
            ChronoUnit.MINUTES).toEpochMilli()))
        .signWith(signatureAlgorithm, signingKey);

    // Add additional claims if specified.
    if (!request.getClaims().keySet().isEmpty()) {
      builder.addClaims(request.getClaims());
    }

    // Populate authorities if they have been supplied. Authorities are collated in a
    // comma-separated string.
    if (!CollectionUtils.isEmpty(request.getAuthorities())) {
      builder.claim(AppConstants.JWT_CLAIM_AUTHORITIES, String.join(",", request.getAuthorities()));
    }

    // Build the JWT and serialize it to a compact, URL-safe string.
    return builder.compact();
  }

  /**
   * Returns the claims of a JWT. This method automatically validates the JWT before a claim is
   * returned.
   *
   * @param jwt The compacted JWT to validate.
   * @return Returns the claims of the JWT, or an encapsulated error message explaining what went
   * wrong when trying to validatete the JWT.
   */
  public JwtClaimsDTO getClaims(String jwt) {
    final JwtClaimsDTO jwtClaimsResponseDTO = new JwtClaimsDTO();

    try {
      jwtClaimsResponseDTO.setClaims(
          Jwts.parser()
              .setSigningKey(appProperties.getJwtSecret().getBytes())
              .setAllowedClockSkewSeconds(appProperties.getJwtSkewAllowed())
              .parseClaimsJws(jwt).getBody());
      jwtClaimsResponseDTO.setValid(true);

    } catch (Exception e) {
      jwtClaimsResponseDTO.setValid(false);
      jwtClaimsResponseDTO.setErrorMessage(e.getMessage());
    }

    return jwtClaimsResponseDTO;
  }

  /**
   * Returns the value of a specific claim in JWT while also verifying the JWT.
   *
   * @param jwt The compacted JWT to validate.
   * @param claim The name of the claim to return.
   * @return The calue of the requested claim.
   */
  private Object getClaimValue(String jwt, String claim) {
    final JwtClaimsDTO claims = getClaims(jwt);
    if (claims != null && claims.getClaims() != null && claims.getClaims().containsKey(claim)) {
      return claims.getClaims().get(claim);
    } else {
      return null;
    }
  }

  /**
   * Extracts the JWT from am `Authorization` HTTP header. The JWT is expected to be prefixed with
   * the word `Bearer`.
   *
   * @param request The {@link HttpServletRequest} bearing the JWT.
   */
  public static String getRawToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    return token != null ? token.replace(AppConstants.AUTH_HEADER_PREFIX, "") : null;
  }
}
