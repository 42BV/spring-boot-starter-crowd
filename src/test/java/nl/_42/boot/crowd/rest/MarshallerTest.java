package nl._42.boot.crowd.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MarshallerTest {

    private final Marshaller marshaller = new Marshaller();

    @Test
    public void shouldSucceed() {
        AuthenticationContext context = new AuthenticationContext();
        context.setUsername("username");
        context.setPassword("password");

        String xml = marshaller.marshall(context);
        Object parsed = marshaller.unmarshal(xml);

        Assertions.assertEquals(AuthenticationContext.class, parsed.getClass());
        Assertions.assertEquals(context.getUsername(), ((AuthenticationContext) parsed).getUsername());
        Assertions.assertEquals(context.getPassword(), ((AuthenticationContext) parsed).getPassword());
    }

}
