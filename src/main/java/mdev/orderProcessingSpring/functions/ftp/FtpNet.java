package mdev.orderProcessingSpring.functions.ftp;

import mdev.orderProcessingSpring.functions.db.Uploader;
import mdev.orderProcessingSpring.utils.models.Item;
import mdev.orderProcessingSpring.utils.models.Order;
import mdev.orderProcessingSpring.utils.vars.OPConfig;
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

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class FtpNet {

    private ConnectionDetail connectionDetail;

    private FTPClient client;

    @Autowired
    public OPConfig OPConfig;

    /**
     * Called to connect to an FTP server
     * @see mdev.orderProcessingSpring.shell.Commands#connectToFtp(String, String, String, String, boolean, boolean)
     *
     * @param connectionDetail the login and connection details
     * @return True if successful connection is made
     * @throws IOException If the connection is corrupted throws an exception
     */
    public boolean connect(ConnectionDetail connectionDetail) throws IOException {
        this.connectionDetail = connectionDetail;
        client = new FTPClient();
        if (connectionDetail != null){
            client.connect(connectionDetail.getHost(), connectionDetail.getPort());
            try {
                OPConfig.getCipher().init(Cipher.DECRYPT_MODE, OPConfig.getcKey());
                String decrypted = new String(OPConfig.getCipher().doFinal(
                        Base64.getDecoder().decode(connectionDetail.getPass().getBytes())));
                return client.login(connectionDetail.getName(), decrypted);
            } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * @return True when the FTP connection is alive
     */
    public boolean isConnected(){
        if (client == null){
            return false;
        }else{
            return client.isConnected();
        }
    }

    /**
     * Called to upload a response file onto the FTP server
     * @see Uploader#upload(Order[], Item[], boolean, boolean)
     *
     * @param file The response file
     * @param connectionDetail The connection details (if reconnection is needed)
     * @return True if successful
     * @throws IOException If the connection is corrupted throws an exception
     */
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

    /**
     * @see mdev.orderProcessingSpring.shell.Commands#disconnectFromFtp(boolean)
     *
     * Called to brake the FTP connection
     * @throws IOException If the connection is corrupted throws an exception
     */
    public void disconnect() throws IOException {
        if (isConnected()){
            client.disconnect();
        }
    }
}
