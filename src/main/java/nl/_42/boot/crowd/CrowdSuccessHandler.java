package nl._42.boot.crowd;

import org.springframework.security.core.Authentication;

public interface CrowdSuccessHandler {

  Authentication onAuthenticated(Authentication authentication);

}
