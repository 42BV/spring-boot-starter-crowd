package nl._42.boot.crowd;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static java.lang.String.format;

@Setter
@ConfigurationProperties("crowd")
public class CrowdProperties {

    private static final String REST_PATH = "/rest/usermanagement/1";

    static final String APPLICATION_PROPERTY = "application.name";
    static final String PASSWORD_PROPERTY = "application.password";
    static final String SERVER_PROPERTY = "server.url";

    private String application = "";
    private String password = "";
    private String server = "";

    @Getter
    private Properties roles = new Properties();

    @PostConstruct
    public void validate() {
        validateProperty(SERVER_PROPERTY, server);
        validateProperty(APPLICATION_PROPERTY, application);
        validateProperty(PASSWORD_PROPERTY, password);
    }

    private void validateProperty(String name, String value) {
        if (value.trim().isEmpty()) {
            throw new IllegalStateException(
                format("Missing required property 'crowd.%s' either specify this property or disable CROWD using 'crowd.enabled=false'", name)
            );
        }
    }

    public String getUrl(String path) {
        return server + REST_PATH + path;
    }

    public void setAuthentication(HttpHeaders headers) {
        headers.setBasicAuth(application, password);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Set<String> groups) {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (String group : groups) {
            String role = roles.getProperty(group, "");
            if (!role.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorities;
    }

}
