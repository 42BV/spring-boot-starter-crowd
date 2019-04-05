package nl._42.boot.crowd;

import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationTest {

    @Autowired
    private RemoteCrowdAuthenticationProvider crowdAuthenticationProvider;

    @Test
    public void loads() {
        assertNotNull(crowdAuthenticationProvider);
    }

    @Test(expected = RuntimeException.class)
    public void login() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user", "password");
        crowdAuthenticationProvider.authenticate(token);
    }

}
