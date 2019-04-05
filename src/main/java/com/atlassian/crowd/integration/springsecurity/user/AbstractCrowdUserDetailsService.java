package com.atlassian.crowd.integration.springsecurity.user;

import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.InvalidAuthorizationTokenException;
import com.atlassian.crowd.exception.UserNotFoundException;
import org.springframework.security.core.GrantedAuthority;

import java.rmi.RemoteException;
import java.util.Collection;

public class AbstractCrowdUserDetailsService extends CrowdUserDetailsServiceImpl {

  @Override
  public Collection<GrantedAuthority> getAuthorities(String username) throws InvalidAuthorizationTokenException, RemoteException, UserNotFoundException, InvalidAuthenticationException {
    return super.getAuthorities(username);
  }

}
