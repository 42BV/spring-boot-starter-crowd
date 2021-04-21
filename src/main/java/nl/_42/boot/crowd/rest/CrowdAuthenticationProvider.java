package nl._42.boot.crowd.rest;

import lombok.extern.slf4j.Slf4j;
import nl._42.boot.crowd.CrowdProperties;
import nl._42.boot.crowd.CrowdSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Slf4j
public class CrowdAuthenticationProvider implements AuthenticationProvider {

    private final CrowdProperties crowdProperties;
    private final CrowdClient crowdClient;

    private CrowdSuccessHandler successHandler;

    public CrowdAuthenticationProvider(CrowdProperties crowdProperties, CrowdClient crowdClient) {
        this.crowdProperties = crowdProperties;
        this.crowdClient = crowdClient;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        // Validate password
        crowdClient.authenticate(username, password);

        // Build authentication
        return getUser(username);
    }

    private Authentication getUser(String username) {
        User user = crowdClient.getUser(username);
        Set<String> groups = crowdClient.getGroups(username);

        Collection<? extends GrantedAuthority> authorities = crowdProperties.getAuthorities(groups);
        CrowdAuthentication authentication = new CrowdAuthentication(user, authorities);

        if (successHandler == null) {
            return authentication;
        }

        return successHandler.onAuthenticated(authentication);
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        return true;
    }

    @Autowired(required = false)
    public void setSuccessHandler(CrowdSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

}
