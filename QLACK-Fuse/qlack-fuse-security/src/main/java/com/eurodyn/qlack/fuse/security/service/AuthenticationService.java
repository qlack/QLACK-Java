package com.eurodyn.qlack.fuse.security.service;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.util.jwt.JWTUtil;
import com.eurodyn.qlack.util.jwt.dto.JWTGenerateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class AuthenticationService {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private UserService userService;

    @Value("${security.jwt.secret:qlackjwtsecret}")
    private String jwtSecret;

    /**
     * Default expiration set at 24 hours.
     */
    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int jwtExpiration;

    /**
     * Provides access to Spring's authentication method.
     *
     * @param authentication Authentication object with user credentials
     * @param applicationSessionId the sessionId of the application the User is using
     * @return generated jwt
     * @throws AuthenticationException If authentication fails
     */
    public String authenticate(Authentication authentication, String applicationSessionId) throws AuthenticationException{
        return generateJWT(authenticationProvider.authenticate(authentication), applicationSessionId);
    }

    /**
     * Provides access to Spring's authentication method.
     *
     * @param user An object containing the username, the password and the sessionId of the User to be authenticated
     * @return generated jwt
     * @throws AuthenticationException If authentication fails
     */
    public String authenticate(UserDetailsDTO user) throws AuthenticationException{
        return generateJWT(authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())),
                user.getSessionId());
    }

    private String generateJWT(Authentication authentication, String applicationSessionId){
        UserDetailsDTO authenticatedUser = (UserDetailsDTO) authentication.getPrincipal();

        String userId = authenticatedUser.getId();
        String sessionId = userService.login(userId, applicationSessionId, true).getSessionId();
        authenticatedUser.setSessionId(sessionId);

        return JWTUtil.generateToken(new JWTGenerateRequestDTO(jwtSecret, authenticatedUser.getUsername(), jwtExpiration));

    }

}
