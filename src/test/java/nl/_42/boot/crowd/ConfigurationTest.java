package nl._42.boot.crowd;

import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ConfigurationTest {

    @Autowired
    private RemoteCrowdAuthenticationProvider crowdAuthenticationProvider;

    @Autowired
    private CrowdUserDetailsServiceImpl crowdUserDetailsService;

    @Test
    public void loads() {
        Assertions.assertNotNull(crowdAuthenticationProvider);
    }

    @Test
    public void login() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user", "password");
        assertThrows(RuntimeException.class, () -> crowdAuthenticationProvider.authenticate(token));
    }

    @Test
    public void mapping() {
        Iterable<Map.Entry<String, String>> iterable = crowdUserDetailsService.getGroupToAuthorityMappings();

        Map<String, String> mappings = new HashMap<>();
        iterable.forEach(entry -> mappings.put(entry.getKey(), entry.getValue()));

        assertEquals(2, mappings.size());
        assertEquals("ROLE_admin", mappings.get("crowd-app-dev-admin"));
        assertEquals("ROLE_user", mappings.get("crowd-app-dev-user"));
    }

}
