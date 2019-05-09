package nl._42.boot.crowd;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Setter
@ConfigurationProperties("crowd")
public class CrowdProperties {

  private static final String SERVER_PROPERTY      = "crowd.server.url";
  private static final String APPLICATION_PROPERTY = "application.name";
  private static final String PASSWORD_PROPERTY    = "application.password";

  private String server;
  private String application;
  private String password;

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
    return properties;
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
