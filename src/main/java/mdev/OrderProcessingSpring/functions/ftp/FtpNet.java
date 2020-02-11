package mdev.OrderProcessingSpring.functions.ftp;

import mdev.OrderProcessingSpring.utils.FinalVars;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.util.Base64;

@Component
public class FtpNet {

    private ConnectionDetail connectionDetail;

    private FTPClient client;

    @Autowired
    public FinalVars finalVars;

    public boolean connect(ConnectionDetail connectionDetail) throws IOException {
        this.connectionDetail = connectionDetail;
        client = new FTPClient();
        client.connect(connectionDetail.getHost(), connectionDetail.getPort());
        try {
            finalVars.getCipher().init(Cipher.DECRYPT_MODE, finalVars.getcKey());
            String decrypted = new String(finalVars.getCipher().doFinal(
                    Base64.getDecoder().decode(connectionDetail.getPass().getBytes())));
            return client.login(connectionDetail.getName(), decrypted);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isConnected(){
        if (client == null){
            return false;
        }else{
            return client.isConnected();
        }
    }

    public boolean upload(File file, ConnectionDetail connectionDetail) throws IOException {
        if (isConnected()){
            InputStream inputStream = new FileInputStream(file);
            return client.storeFile(file.getName(), inputStream);
        }else{
            if(connect(connectionDetail)){
                return upload(file, connectionDetail);
            }else{
                return false;
            }
        }
    }

    public ConnectionDetail getConnectionDetail() {
        return connectionDetail;
    }

    public void disconnect() throws IOException {
        if (isConnected()){
            client.disconnect();
        }
    }
}
