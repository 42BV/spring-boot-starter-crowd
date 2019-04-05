package nl._42.boot.crowd;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

class CrowdRoleMapper {

  @Setter
  private Properties roles = new Properties();

  @Setter
  private String prefix = "";

  public Collection<GrantedAuthority> getAuthorities(Collection<GrantedAuthority> authorities) {
    return authorities.stream()
      .map(GrantedAuthority::getAuthority)
      .map(authority -> roles.getProperty(authority))
      .filter(StringUtils::isNotBlank)
      .distinct().sorted()
      .map(role -> new SimpleGrantedAuthority(prefix + role))
      .collect(Collectors.toSet());
  }

}
