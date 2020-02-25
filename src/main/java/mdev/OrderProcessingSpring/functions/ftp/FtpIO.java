package mdev.OrderProcessingSpring.functions.ftp;

import mdev.OrderProcessingSpring.OPSpringApp;
import mdev.OrderProcessingSpring.shell.Commands;
import mdev.OrderProcessingSpring.utils.FinalVars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.security.InvalidKeyException;
import java.util.Base64;
import java.util.Properties;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class FtpIO {

    private Commands commands;

    @Autowired
    public FinalVars finalVars;

    /**
     * Called to save the ftp login details
     * @see mdev.OrderProcessingSpring.functions.CommandFunctions#saveFtp(String, int, String, String)
     * @see Commands#saveFtpLogin(String, int, String, String)
     *
     * @param url The server host
     * @param port The port
     * @param name The username
     * @param pass The password
     * @return The result of the process
     * @throws InvalidKeyException The cipher can throw this exception (used for password encoding)
     * @throws BadPaddingException The cipher can throw this exception (used for password encoding)
     * @throws IllegalBlockSizeException The cipher can throw this exception (used for password encoding)
     */
    public String saveFtp(String url, int port, String name, String pass) throws InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        Properties properties = new Properties();
        properties.put(finalVars.FTP_SERVER_KEY, url);
        properties.put(finalVars.FTP_PORT_KEY, port + "");
        properties.put(finalVars.FTP_USER_KEY, name);
        finalVars.getCipher().init(Cipher.ENCRYPT_MODE, finalVars.getcKey());
        properties.put(finalVars.FTP_PASS_KEY, new String(Base64.getEncoder().encode(finalVars.getCipher().doFinal(pass.getBytes()))));

        return writeFtpDetails(properties);
    }

    /**
     * If the FTP connection is already alive, OP creates a ConnectionDetail object which contains all of the
     * login details, makes saving it easier..
     * @param connectionDetail The connection details
     * @return The process results
     * @throws InvalidKeyException The cipher can throw this exception (used for password encoding)
     */
    public String saveFtp(ConnectionDetail connectionDetail) throws InvalidKeyException {
        Properties properties = new Properties();
        properties.put(finalVars.FTP_SERVER_KEY, connectionDetail.getHost());
        properties.put(finalVars.FTP_PORT_KEY, connectionDetail.getPort() + "");
        properties.put(finalVars.FTP_USER_KEY, connectionDetail.getName());
        finalVars.getCipher().init(Cipher.ENCRYPT_MODE, finalVars.getcKey());
        properties.put(finalVars.FTP_PASS_KEY, connectionDetail.getPass());

        return writeFtpDetails(properties);
    }

    /**
     * Writes the login details to a properties file and saves it
     * @param properties The properties to write into a file
     * @return The results of the process
     */
    private String writeFtpDetails(Properties properties){
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(finalVars.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            properties.store(outputStream, null);
        } catch (IOException e) {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(e1.toString()));
                }
            }
            OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(e.toString()));
            File file = new File(finalVars.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            try {
                if(file.createNewFile()){
                    outputStream = new FileOutputStream(file);
                    properties.store(outputStream, null);
                }else{
                    OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage("Could not save FTP login details!"));
                    return "";
                }
            } catch (IOException ex1) {
                OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(ex1.toString()));
                if (outputStream != null){
                    try {
                        outputStream.close();
                    } catch (IOException e1) {
                        OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(e1.toString()));
                    }
                    return "";
                }
            }
        }finally {
            try {
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "FTP login details saved!";
    }

    /**
     * Removes the saved details if the connection details file exists..
     */
    public void removeFtp(){
        try{
            File file = new File(finalVars.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            if (file.exists()){
                if (file.delete()){
                    OPSpringApp.log.info(commands.shellUsrEX.getSuccessMessage("FTP details removed."));
                }
            }else{
                OPSpringApp.log.warn(commands.shellUsrEX.getWarningMessage("The file did not exist.."));
            }
        }catch(Exception ex){
            OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(ex.toString()));
        }
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    /**
     * Called to load login details for auto login
     * @return The ConnectionDetail object
     */
    public ConnectionDetail loadFromFile(){
        ConnectionDetail connectionDetail = null;
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(finalVars.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties")) {

            properties.load(is);
            connectionDetail = new ConnectionDetail(
                    properties.getProperty(finalVars.FTP_SERVER_KEY),
                    Integer.parseInt(properties.getProperty(finalVars.FTP_PORT_KEY)),
                    properties.getProperty(finalVars.FTP_USER_KEY),
                    properties.getProperty(finalVars.FTP_PASS_KEY));

        } catch (IOException ex) {
            OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(ex.toString()));
        }
        return connectionDetail;
    }

}
