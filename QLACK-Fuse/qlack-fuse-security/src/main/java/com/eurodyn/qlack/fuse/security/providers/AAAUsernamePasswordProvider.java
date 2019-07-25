package com.eurodyn.qlack.fuse.security.providers;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.security.cache.AAAUserCaching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author EUROPEAN DYNAMICS SA
 */
public class AAAUsernamePasswordProvider extends DaoAuthenticationProvider {

    @Autowired
    private AAAUserCaching caching;

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        super.doAfterPropertiesSet();

        // Set the default user cache.
        setUserCache(caching.getUserCache());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                "AaaProvider.badCredentials",
                "Bad credentials"));
        }

        String presentedPassword;
      UserDetailsDTO user = (UserDetailsDTO) userDetails;

      if (user.getSalt() != null) {
        presentedPassword = user.getSalt() + authentication.getCredentials().toString();
        } else {
        presentedPassword = authentication.getCredentials().toString();
        }

        if (!getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                "AaaProvider.badCredentials",
                "Bad credentials"));
        }
    }

}
