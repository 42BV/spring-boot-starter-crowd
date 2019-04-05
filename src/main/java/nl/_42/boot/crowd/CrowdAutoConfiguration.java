package nl._42.boot.crowd;

import com.atlassian.crowd.integration.http.HttpAuthenticator;
import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService;
import com.atlassian.crowd.service.AuthenticationManager;
import com.atlassian.crowd.service.GroupMembershipManager;
import com.atlassian.crowd.service.UserManager;
import com.atlassian.crowd.service.soap.client.SoapClientPropertiesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * Automatically registers a CROWD authentication provider.
 */
@Configuration
@ConditionalOnProperty(name = "crowd.enabled", havingValue = "true", matchIfMissing = true)
@ImportResource("classpath:/applicationContext-CrowdClient.xml")
public class CrowdAutoConfiguration {

  private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
  private static final String ROLE_PREFIX_NAME = "crowd.role_prefix";

  @Autowired
  private HttpAuthenticator httpAuthenticator;

  @Autowired
  private GroupMembershipManager groupMembershipManager;

  @Autowired
  @Qualifier("crowdAuthenticationManager")
  private AuthenticationManager crowdAuthenticationManager;

  @Autowired
  private UserManager userManager;

  @Bean
  public RemoteCrowdAuthenticationProvider crowdAuthenticationProvider(Environment environment) {
    CrowdUserDetailsService userDetailsService = crowdUserDetailsService(environment);
    return new RemoteCrowdAuthenticationProvider(crowdAuthenticationManager, httpAuthenticator, userDetailsService);
  }

  private CrowdUserDetailsService crowdUserDetailsService(Environment environment) {
    CrowdRoleMapper mapper = crowdRoleMapper(environment);

    CrowdUserDetailsMappingService userDetailService = new CrowdUserDetailsMappingService(mapper);
    userDetailService.setGroupMembershipManager(groupMembershipManager);
    userDetailService.setUserManager(userManager);
    userDetailService.setAuthorityPrefix(""); // Included by role mapper
    return userDetailService;
  }

  private CrowdRoleMapper crowdRoleMapper(Environment environment) {
    CrowdRoleMapper mapper = new CrowdRoleMapper();
    mapper.setPrefix(environment.getProperty(ROLE_PREFIX_NAME, DEFAULT_ROLE_PREFIX));
    mapper.setRoles(crowdRoles());
    return mapper;
  }

  @Bean
  @ConfigurationProperties(prefix = "crowd.roles")
  public Properties crowdRoles() {
    return new Properties();
  }

  @Bean
  @ConfigurationProperties(prefix = "crowd.property")
  public Properties crowdProperties() {
    return new Properties();
  }

  @Configuration
  static class CrowdUpdatePropertiesConfig {

    /**
     * Sets the crowd properties defined in the application yml in the clientProperties instance of Atlassian.
     * This way you do need the crowd.properties file in your classpath.
     * @param crowdProperties A bean holding crowd properties of the application.yml
     * @param clientProperties The Atlassian bean that holds the crowd properties used by their crowd authenticator.
     */
    @Autowired
    CrowdUpdatePropertiesConfig(Properties crowdProperties, SoapClientPropertiesImpl clientProperties) {
      clientProperties.updateProperties(crowdProperties);
    }

  }

}
