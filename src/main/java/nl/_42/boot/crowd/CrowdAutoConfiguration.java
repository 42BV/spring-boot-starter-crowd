package nl._42.boot.crowd;

import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticatorImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelper;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelperImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractorImpl;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl;
import com.atlassian.crowd.service.client.ClientProperties;
import com.atlassian.crowd.service.client.ClientPropertiesImpl;
import com.atlassian.crowd.service.client.CrowdClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * Automatically registers a CROWD authentication provider.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CrowdProperties.class)
@ConditionalOnResource(resources = { "classpath:/applicationContext-CrowdRestClient.xml" })
@ConditionalOnProperty(name = "crowd.enabled", havingValue = "true", matchIfMissing = true)
public class CrowdAutoConfiguration {

    @Autowired
    private CrowdProperties properties;

    @Bean
    public ClientProperties clientProperties() {
        return ClientPropertiesImpl.newInstanceFromProperties(
            properties.getProperties()
        );
    }

    @Bean
    public CrowdClient crowdClient(ClientProperties properties) {
        return new RestCrowdClientFactory().newInstance(properties);
    }

    @Bean
    public RemoteCrowdAuthenticationProvider crowdAuthenticationProvider(CrowdClient client, CrowdHttpAuthenticator authenticator, CrowdUserDetailsService userDetailsService) {
        return new RemoteCrowdAuthenticationProvider(client, authenticator, userDetailsService);
    }

    @Bean
    public CrowdHttpAuthenticator crowdHttpAuthenticator(CrowdClient client, ClientProperties properties) {
        return new CrowdHttpAuthenticatorImpl(
            client,
            properties,
            crowdTokenHelper()
        );
    }

    private CrowdHttpTokenHelper crowdTokenHelper() {
        return CrowdHttpTokenHelperImpl.getInstance(
            CrowdHttpValidationFactorExtractorImpl.getInstance()
        );
    }

    @Bean
    public CrowdUserDetailsService crowdUserDetailsService(CrowdClient client) {
        CrowdUserDetailsServiceImpl userDetailsService = new CrowdUserDetailsServiceImpl();
        userDetailsService.setCrowdClient(client);
        setGroupToAuthorityMappings(userDetailsService);
        return userDetailsService;
    }

    private void setGroupToAuthorityMappings(CrowdUserDetailsServiceImpl userDetailsService) {
        Set<Map.Entry<String, String>> mappings = properties.getGroupToAuthorityMappings();

        if (!mappings.isEmpty()) {
            log.info("Found 'crowd.roles' in spring boot application properties.");
            mappings.forEach(mapping -> log.info("\t {}", mapping));
            userDetailsService.setGroupToAuthorityMappings(mappings);
        } else {
            log.warn("No 'crowd.roles' found in spring boot application properties, no conversion of Crowd Groups will be applied!");
        }
    }

}
