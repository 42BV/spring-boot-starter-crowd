package nl._42.boot.crowd;

import nl._42.boot.crowd.rest.CrowdAuthentication;
import org.springframework.security.core.Authentication;

public interface CrowdSuccessHandler {

  Authentication onAuthenticated(CrowdAuthentication authentication);

}
