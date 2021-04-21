package nl._42.boot.crowd;

import lombok.extern.slf4j.Slf4j;
import nl._42.boot.crowd.rest.CrowdAuthenticationProvider;
import nl._42.boot.crowd.rest.CrowdClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Automatically registers a CROWD authentication provider.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CrowdProperties.class)
@ConditionalOnProperty(name = "crowd.enabled", havingValue = "true", matchIfMissing = true)
public class CrowdAutoConfiguration {

    @Bean
    public CrowdClient crowdClient(CrowdProperties crowdProperties) {
        return new CrowdClient(crowdProperties);
    }

    @Bean
    public CrowdAuthenticationProvider crowdAuthenticationProvider(CrowdProperties crowdProperties, CrowdClient crowdClient) {
        return new CrowdAuthenticationProvider(crowdProperties, crowdClient);
    }

}
