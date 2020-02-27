package mdev.orderProcessingSpring.utils.vars;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class OPConfig {

    @Value("${ftp.pass.c.key}")
    private String cKey;

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
