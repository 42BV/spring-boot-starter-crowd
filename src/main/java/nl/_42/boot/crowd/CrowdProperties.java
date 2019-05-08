package nl._42.boot.crowd;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Getter
@Setter
@ConfigurationProperties("crowd")
public class CrowdProperties {

  private Properties properties = new Properties();
  private Properties roles = new Properties();

  /**
   * Convert the group to authority properties into a set of map entries to please the Atlassian API.
   * @return the set of map entries, each representing a group to authority mapping
   */
  public Set<Map.Entry<String, String>> getGroupToAuthorityMappings() {
    Stream<Map.Entry<Object, Object>> entries = roles.entrySet().stream();
    return entries.collect(toMap(e -> e.getKey().toString(), e -> e.getValue().toString())).entrySet();
  }

}
