package mdev.orderProcessingSpring.functions.ftp;

import ch.qos.logback.classic.Logger;
import mdev.orderProcessingSpring.shell.Commands;
import mdev.orderProcessingSpring.utils.vars.FtpPropCodes;
import mdev.orderProcessingSpring.utils.vars.OPConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    private Logger logger;

    @PostConstruct
    private void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(FtpIO.class);
    }

    private Commands commands;

    @Autowired
    private FtpPropCodes ftpPropCodes;

    @Autowired
    private OPConfig opConfig;

    /**
     * Called to save the ftp login details
     * @see mdev.orderProcessingSpring.functions.CommandFunctions#saveFtp(String, int, String, String)
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
        properties.put(ftpPropCodes.FTP_SERVER_KEY, url);
        properties.put(ftpPropCodes.FTP_PORT_KEY, port + "");
        properties.put(ftpPropCodes.FTP_USER_KEY, name);
        opConfig.getCipher().init(Cipher.ENCRYPT_MODE, opConfig.getcKey());
        properties.put(ftpPropCodes.FTP_PASS_KEY, new String(Base64.getEncoder().encode(opConfig.getCipher().doFinal(pass.getBytes()))));

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
        properties.put(ftpPropCodes.FTP_SERVER_KEY, connectionDetail.getHost());
        properties.put(ftpPropCodes.FTP_PORT_KEY, connectionDetail.getPort() + "");
        properties.put(ftpPropCodes.FTP_USER_KEY, connectionDetail.getName());
        opConfig.getCipher().init(Cipher.ENCRYPT_MODE, opConfig.getcKey());
        properties.put(ftpPropCodes.FTP_PASS_KEY, connectionDetail.getPass());

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
            outputStream = new FileOutputStream(ftpPropCodes.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            properties.store(outputStream, null);
        } catch (IOException e) {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    logger.error(commands.shellUsrEX.getErrorMessage(e1.toString()));
                }
            }
            logger.error(commands.shellUsrEX.getErrorMessage(e.toString()));
            File file = new File(ftpPropCodes.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            try {
                if(file.createNewFile()){
                    outputStream = new FileOutputStream(file);
                    properties.store(outputStream, null);
                }else{
                    logger.error(commands.shellUsrEX.getErrorMessage("Could not save FTP login details!"));
                    return "";
                }
            } catch (IOException ex1) {
                logger.error(commands.shellUsrEX.getErrorMessage(ex1.toString()));
                if (outputStream != null){
                    try {
                        outputStream.close();
                    } catch (IOException e1) {
                        logger.error(commands.shellUsrEX.getErrorMessage(e1.toString()));
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
            File file = new File(ftpPropCodes.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            if (file.exists()){
                if (file.delete()){
                    logger.info(commands.shellUsrEX.getSuccessMessage("FTP details removed."));
                }
            }else{
                logger.warn(commands.shellUsrEX.getWarningMessage("The file did not exist.."));
            }
        }catch(Exception ex){
            logger.error(commands.shellUsrEX.getErrorMessage(ex.toString()));
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
        try (InputStream is = new FileInputStream(ftpPropCodes.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties")) {

            properties.load(is);
            connectionDetail = new ConnectionDetail(
                    properties.getProperty(ftpPropCodes.FTP_SERVER_KEY),
                    Integer.parseInt(properties.getProperty(ftpPropCodes.FTP_PORT_KEY)),
                    properties.getProperty(ftpPropCodes.FTP_USER_KEY),
                    properties.getProperty(ftpPropCodes.FTP_PASS_KEY));

        } catch (IOException ex) {
            logger.error(commands.shellUsrEX.getErrorMessage(ex.toString()));
        }
        return connectionDetail;
    }

}
