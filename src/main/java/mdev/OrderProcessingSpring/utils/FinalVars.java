package mdev.OrderProcessingSpring.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FinalVars {

    public final String FTP_SERVER_KEY = "server";
    public final String FTP_PORT_KEY = "port";
    public final String FTP_USER_KEY = "u";
    public final String FTP_PASS_KEY = "p";
    public final String FTP_CONNECTION_DETAILS_PROPERTIES_NAME = "ftp_details";

    private BCryptPasswordEncoder encrypt;

    @PostConstruct
    public void initializeEncrypt(){
        encrypt = new BCryptPasswordEncoder();
    }

    public BCryptPasswordEncoder getEncrypt() {
        return encrypt;
    }
}
