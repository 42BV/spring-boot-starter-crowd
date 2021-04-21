package nl._42.boot.crowd.rest;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class CrowdAuthentication extends AbstractAuthenticationToken {

    private final User user;

    public CrowdAuthentication(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return user.getName();
    }

    @Override
    public Object getCredentials() {
        return "";
    }

}
