package mdev.OrderProcessingSpring.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Component
public class FinalVars {

    @Value("${ftp.pass.c.key}")
    private String cKey;

    public final String FTP_SERVER_KEY = "server";
    public final String FTP_PORT_KEY = "port";
    public final String FTP_USER_KEY = "u";
    public final String FTP_PASS_KEY = "p";
    public final String FTP_CONNECTION_DETAILS_PROPERTIES_NAME = "ftp_details";

    public final int DEFAULT_FTP_PORT = 21;

    private Cipher cipher;

    @PostConstruct
    public void initCipher(){
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public Cipher getCipher() {
        return cipher;
    }

    public Key getcKey() {
        return new SecretKeySpec(cKey.getBytes(), "AES");
    }
}
