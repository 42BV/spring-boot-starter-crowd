package nl._42.boot.crowd;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

@Setter
@ConfigurationProperties("crowd")
public class CrowdProperties {

    static final String SERVER_PROPERTY = "crowd.server.url";
    static final String APPLICATION_PROPERTY = "application.name";
    static final String PASSWORD_PROPERTY = "application.password";

    private String server = "";
    private String application = "";
    private String password = "";

    private Properties properties = new Properties();
    private Properties roles = new Properties();

    /**
     * Retrieve the CROWD properties.
     * @return the properties
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put(SERVER_PROPERTY, server);
        properties.put(APPLICATION_PROPERTY, application);
        properties.put(PASSWORD_PROPERTY, password);
        this.properties.forEach(properties::put);

        verifyProperties(properties);

        return properties;
    }

    private void verifyProperties(Properties properties) {
        verifyProperty(properties, SERVER_PROPERTY);
        verifyProperty(properties, APPLICATION_PROPERTY);
        verifyProperty(properties, PASSWORD_PROPERTY);
    }

    private void verifyProperty(Properties properties, String name) {
        String value = properties.getProperty(name);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalStateException(
                format("Missing required property 'crowd.properties.%s' either specify " +
                       "this property or disable CROWD using 'crowd.enabled=false'", name)
            );
        }
    }

    /**
     * Convert the group to authority properties into a set of map entries to please the Atlassian API.
     * @return the set of map entries, each representing a group to authority mapping
     */
    public Set<Map.Entry<String, String>> getGroupToAuthorityMappings() {
        Stream<Map.Entry<Object, Object>> entries = roles.entrySet().stream();
        return entries.collect(toMap(e -> e.getKey().toString(), e -> e.getValue().toString())).entrySet();
    }

}
