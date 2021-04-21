package nl._42.boot.crowd;

import nl._42.boot.crowd.rest.CrowdAuthenticationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ConfigurationTest {

    @Autowired
    private CrowdAuthenticationProvider crowdAuthenticationProvider;

    @Autowired
    private CrowdProperties crowdProperties;

    @Test
    public void loads() {
        Assertions.assertNotNull(crowdAuthenticationProvider);
    }

    @Test
    public void login() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("jeroen", "password");
        assertThrows(AuthenticationServiceException.class, () -> crowdAuthenticationProvider.authenticate(token));
    }

    @Test
    public void mapping() {
        Properties roles = crowdProperties.getRoles();

        assertEquals(3, roles.size());
        assertEquals("ROLE_admin", roles.getProperty("42-devs"));
        assertEquals("ROLE_admin", roles.getProperty("crowd-app-dev-admin"));
        assertEquals("ROLE_user", roles.getProperty("crowd-app-dev-user"));
    }

}
