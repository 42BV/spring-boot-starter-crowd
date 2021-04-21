package nl._42.boot.crowd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CrowdPropertiesTest {

    @Test
    public void validate() {
        CrowdProperties properties = new CrowdProperties();
        properties.setServer("http://mycrowd");
        properties.setApplication("application");
        properties.setPassword("password");

        properties.validate();
    }

    @Test
    public void validate_missing_server() {
        CrowdProperties properties = new CrowdProperties();
        properties.setApplication("application");
        properties.setPassword("password");

        try {
            properties.validate();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            assertEquals(
                    "Missing required property 'crowd.server.url' either specify "
                            + "this property or disable CROWD using 'crowd.enabled=false'",
                ise.getMessage()
            );
        }
    }

    @Test
    public void validate_missing_application() {
        CrowdProperties properties = new CrowdProperties();
        properties.setServer("http://mycrowd");
        properties.setPassword("password");

        try {
            properties.validate();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            assertEquals(
                    "Missing required property 'crowd.application.name' either specify "
                            + "this property or disable CROWD using 'crowd.enabled=false'",
                    ise.getMessage()
            );
        }
    }

    @Test
    public void validate_missing_password() {
        CrowdProperties properties = new CrowdProperties();
        properties.setServer("http://mycrowd");
        properties.setApplication("application");

        try {
            properties.validate();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            assertEquals(
                    "Missing required property 'crowd.application.password' either specify "
                            + "this property or disable CROWD using 'crowd.enabled=false'",
                    ise.getMessage()
            );
        }
    }

}
