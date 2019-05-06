package nl._42.boot.crowd;

import com.atlassian.crowd.integration.http.HttpAuthenticator;
import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl;
import com.atlassian.crowd.service.AuthenticationManager;
import com.atlassian.crowd.service.GroupMembershipManager;
import com.atlassian.crowd.service.UserManager;
import com.atlassian.crowd.service.soap.client.SoapClientPropertiesImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

/**
 * Automatically registers a CROWD authentication provider.
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "crowd.enabled", havingValue = "true", matchIfMissing = true)
@ImportResource("classpath:/applicationContext-CrowdClient.xml")
public class CrowdAutoConfiguration {

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
  public RemoteCrowdAuthenticationProvider crowdAuthenticationProvider() {
    return new RemoteCrowdAuthenticationProvider(crowdAuthenticationManager, httpAuthenticator, crowdUserDetailsService());
  }

  @Bean
  public CrowdUserDetailsService crowdUserDetailsService() {
    CrowdUserDetailsServiceImpl crowdUserDetailsService = new CrowdUserDetailsServiceImpl();
    crowdUserDetailsService.setGroupMembershipManager(groupMembershipManager);
    crowdUserDetailsService.setUserManager(userManager);

    Set<Map.Entry<String, String>> roleMappings = getCrowdGroupToAuthorityMappings();
    if (!roleMappings.isEmpty()) {
      log.info("Found 'crowd.roles' in spring boot application properties.");
      roleMappings.forEach(roleMapping -> log.info("\t {}", roleMapping));
      crowdUserDetailsService.setGroupToAuthorityMappings(roleMappings);
    } else {
      log.warn("No 'crowd.roles' found in spring boot application properties, no conversion of Crowd Groups will be applied!");
    }

    return crowdUserDetailsService;
  }

  private Set<Map.Entry<String, String>> getCrowdGroupToAuthorityMappings() {
    return crowdRoles().entrySet().stream().collect(toMap(
        e -> e.getKey().toString(),
        e -> e.getValue().toString()
      )).entrySet();
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
