package nl._42.boot.crowd;

import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.InvalidAuthorizationTokenException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.integration.springsecurity.user.AbstractCrowdUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.rmi.RemoteException;
import java.util.Collection;

@AllArgsConstructor
public class CrowdUserDetailsMappingService extends AbstractCrowdUserDetailsService {

  private final CrowdRoleMapper mapper;

  @Override
  public Collection<GrantedAuthority> getAuthorities(String username) throws InvalidAuthorizationTokenException, RemoteException, UserNotFoundException, InvalidAuthenticationException {
    Collection<GrantedAuthority> authorities = super.getAuthorities(username);
    return mapper.getAuthorities(authorities);
  }
  
}
