package nl._42.boot.crowd.rest;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "authentication-context")
@XmlAccessorType(XmlAccessType.FIELD)
class AuthenticationContext {

    private String username;
    private String password;

}
