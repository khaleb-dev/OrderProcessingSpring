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
        Properties p = new Properties();
        p.put(finalVars.FTP_SERVER_KEY, url);
        p.put(finalVars.FTP_PORT_KEY, port + "");
        p.put(finalVars.FTP_USER_KEY, name);
        finalVars.getCipher().init(Cipher.ENCRYPT_MODE, finalVars.getcKey());
        p.put(finalVars.FTP_PASS_KEY, new String(Base64.getEncoder().encode(finalVars.getCipher().doFinal(pass.getBytes()))));

        return writeFtpDetails(p);
    }

    /**
     * If the FTP connection is already alive, OP creates a ConnectionDetail object which contains all of the
     * login details, makes saving it easier..
     * @param cd The connection details
     * @return The process results
     * @throws InvalidKeyException The cipher can throw this exception (used for password encoding)
     */
    public String saveFtp(ConnectionDetail cd) throws InvalidKeyException {
        Properties p = new Properties();
        p.put(finalVars.FTP_SERVER_KEY, cd.getHost());
        p.put(finalVars.FTP_PORT_KEY, cd.getPort() + "");
        p.put(finalVars.FTP_USER_KEY, cd.getName());
        finalVars.getCipher().init(Cipher.ENCRYPT_MODE, finalVars.getcKey());
        p.put(finalVars.FTP_PASS_KEY, cd.getPass());

        return writeFtpDetails(p);
    }

    /**
     * Writes the login details to a properties file and saves it
     * @param p The properties to write into a file
     * @return The results of the process
     */
    private String writeFtpDetails(Properties p){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(finalVars.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            p.store(fos, null);
        } catch (IOException e) {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e1) {
                    OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(e1.toString()));
                }
            }
            OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(e.toString()));
            File f = new File(finalVars.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            try {
                if(f.createNewFile()){
                    fos = new FileOutputStream(f);
                    p.store(fos, null);
                }else{
                    OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage("Could not save FTP login details!"));
                    return "";
                }
            } catch (IOException ex1) {
                OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(ex1.toString()));
                if (fos != null){
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(e1.toString()));
                    }
                    return "";
                }
            }
        }finally {
            try {
                if (fos != null){
                    fos.close();
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
            File f = new File(finalVars.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties");
            if (f.exists()){
                if (f.delete()){
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
        ConnectionDetail cd = null;
        Properties p = new Properties();
        try (InputStream is = new FileInputStream(finalVars.FTP_CONNECTION_DETAILS_PROPERTIES_NAME + ".properties")) {

            p.load(is);
            cd = new ConnectionDetail(
                    p.getProperty(finalVars.FTP_SERVER_KEY),
                    Integer.parseInt(p.getProperty(finalVars.FTP_PORT_KEY)),
                    p.getProperty(finalVars.FTP_USER_KEY),
                    p.getProperty(finalVars.FTP_PASS_KEY));

        } catch (IOException ex) {
            OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(ex.toString()));
        }
        return cd;
    }

}
