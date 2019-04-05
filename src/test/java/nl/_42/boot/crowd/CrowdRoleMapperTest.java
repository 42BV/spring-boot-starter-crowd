package nl._42.boot.crowd;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class CrowdRoleMapperTest {

  private CrowdRoleMapper mapper;
  private Properties roles;

  @Before
  public void setUp() {
    mapper = new CrowdRoleMapper();
    mapper.setPrefix("ROLE_");

    roles = new Properties();
    roles.put("myapp-dev-admin", "ADMIN");
    mapper.setRoles(roles);
  }

  @Test
  public void map() {
    GrantedAuthority authority = new SimpleGrantedAuthority("myapp-dev-admin");

    Collection<GrantedAuthority> mapped = mapper.getAuthorities(Collections.singleton(authority));
    assertEquals(Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")), mapped);
  }

  @Test
  public void ignore() {
    GrantedAuthority authority = new SimpleGrantedAuthority("myapp-dev-user");

    Collection<GrantedAuthority> mapped = mapper.getAuthorities(Collections.singleton(authority));
    assertEquals(Collections.emptySet(), mapped);
  }

}
