package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.functions.ftp.FtpNet;
import mdev.OrderProcessingSpring.utils.DataRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class Uploader {

    @Autowired
    public FtpNet ftpNet;

    public String upload(DataRow[] dataRows, boolean uploadResponseToFtp){

        if (uploadResponseToFtp){
            //noinspection StatementWithEmptyBody
            if (!ftpNet.isConnected() && ftpNet.getConnectionDetail() != null){
                try {
                    ftpNet.connect(ftpNet.getConnectionDetail());
                    // Upload the response
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                // Upload the response
            }
        }
        return "";
    }

}
