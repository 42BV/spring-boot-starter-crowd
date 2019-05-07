package nl._42.boot.crowd;

import com.atlassian.crowd.integration.http.HttpAuthenticator;
import com.atlassian.crowd.integration.http.HttpAuthenticatorImpl;
import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl;
import com.atlassian.crowd.service.AuthenticationManager;
import com.atlassian.crowd.service.GroupManager;
import com.atlassian.crowd.service.GroupMembershipManager;
import com.atlassian.crowd.service.UserManager;
import com.atlassian.crowd.service.cache.BasicCache;
import com.atlassian.crowd.service.cache.CacheAwareAuthenticationManager;
import com.atlassian.crowd.service.cache.CacheImpl;
import com.atlassian.crowd.service.cache.CachingGroupManager;
import com.atlassian.crowd.service.cache.CachingGroupMembershipManager;
import com.atlassian.crowd.service.cache.CachingUserManager;
import com.atlassian.crowd.service.soap.client.SecurityServerClient;
import com.atlassian.crowd.service.soap.client.SecurityServerClientImpl;
import com.atlassian.crowd.service.soap.client.SoapClientPropertiesImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Automatically registers a CROWD authentication provider.
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "crowd.enabled", havingValue = "true", matchIfMissing = true)
public class CrowdAutoConfiguration {

  @Bean
  public RemoteCrowdAuthenticationProvider crowdAuthenticationProvider(AuthenticationManager authenticationManager, HttpAuthenticator httpAuthenticator, CrowdUserDetailsService userDetailsService) {
    return new RemoteCrowdAuthenticationProvider(authenticationManager, httpAuthenticator, userDetailsService);
  }

  @Bean
  public CrowdUserDetailsService crowdUserDetailsService(GroupMembershipManager groupMembershipManager, UserManager userManager) {
    CrowdUserDetailsServiceImpl userDetailsService = new CrowdUserDetailsServiceImpl();
    userDetailsService.setGroupMembershipManager(groupMembershipManager);
    userDetailsService.setUserManager(userManager);
    setGroupToAuthorityMappings(userDetailsService);
    return userDetailsService;
  }

  private void setGroupToAuthorityMappings(CrowdUserDetailsServiceImpl userDetailsService) {
    Set<Map.Entry<String, String>> roles = getCrowdRolesMapping();

    if (!roles.isEmpty()) {
      log.info("Found 'crowd.roles' in spring boot application properties.");
      roles.forEach(roleMapping -> log.info("\t {}", roleMapping));
      userDetailsService.setGroupToAuthorityMappings(roles);
    } else {
      log.warn("No 'crowd.roles' found in spring boot application properties, no conversion of Crowd Groups will be applied!");
    }
  }

  private Set<Map.Entry<String, String>> getCrowdRolesMapping() {
    Stream<Map.Entry<Object, Object>> entries = crowdRoles().entrySet().stream();
    return entries.collect(toMap(e -> e.getKey().toString(), e -> e.getValue().toString())).entrySet();
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

  @Bean
  public SecurityServerClient crowdSoapServerClient() {
    SoapClientPropertiesImpl properties = SoapClientPropertiesImpl.newInstanceFromProperties(crowdProperties());
    return new SecurityServerClientImpl(properties);
  }

  @Bean
  public AuthenticationManager crowdAuthenticationManager(SecurityServerClient securityServerClient, UserManager userManager) {
    return new CacheAwareAuthenticationManager(securityServerClient, userManager);
  }

  @Bean
  public HttpAuthenticator crowdAuthenticator(AuthenticationManager authenticationManager) {
    return new HttpAuthenticatorImpl(authenticationManager);
  }

  @Bean
  public CacheManager crowdCacheManager() {
    EhCacheManagerFactoryBean manager = new EhCacheManagerFactoryBean();
    manager.setConfigLocation(new ClassPathResource("crowd-ehcache.xml"));
    manager.afterPropertiesSet();
    return manager.getObject();
  }

  @Bean
  public BasicCache crowdCache(CacheManager crowdCacheManager) {
    return new CacheImpl(crowdCacheManager);
  }

  @Bean
  public UserManager crowdUserManager(SecurityServerClient securityServerClient, BasicCache cache) {
    return new CachingUserManager(securityServerClient, cache);
  }

  @Bean
  public GroupManager crowdGroupManager(SecurityServerClient securityServerClient, BasicCache cache) {
    return new CachingGroupManager(securityServerClient, cache);
  }

  @Bean
  public GroupMembershipManager crowdGroupMembershipManager(SecurityServerClient securityServerClient, UserManager userManager, GroupManager groupManager, BasicCache cache) {
    return new CachingGroupMembershipManager(securityServerClient, userManager, groupManager, cache);
  }

}
