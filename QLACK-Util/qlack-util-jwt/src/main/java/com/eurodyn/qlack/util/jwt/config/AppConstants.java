package com.eurodyn.qlack.util.jwt.config;

/**
 * Application constants defining standard parameters for the application.
 */
public class AppConstants {
  // The prefix of the HTTP Authorisation header before the value of the JWT.
  public static final String AUTH_HEADER_PREFIX = "Bearer ";

  // The name of the claim bearing the authorities of the user under JWT's claims.
  public static final String JWT_CLAIM_AUTHORITIES ="authorities";
}
