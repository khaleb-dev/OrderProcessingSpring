package mdev.OrderProcessingSpring.shell;

import mdev.OrderProcessingSpring.functions.CommandFunctions;
import mdev.OrderProcessingSpring.functions.ftp.ConnectionDetail;
import mdev.OrderProcessingSpring.functions.ftp.FtpIO;
import mdev.OrderProcessingSpring.functions.ftp.FtpNet;
import mdev.OrderProcessingSpring.utils.FinalVars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.Base64;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@ShellComponent
public class Commands {

    private final FinalVars finalVars;
    private final FtpNet ftpNet;
    private final FtpIO ftpIO;
    private final CommandFunctions commandFunctions;
    public final ShellUsrEX shellUsrEX;

    @Autowired
    public Commands(CommandFunctions commandFunctions, ShellUsrEX shellUsrEX,
                    FtpNet ftpNet, FinalVars finalVars, FtpIO ftpIO) {
        this.commandFunctions = commandFunctions;
        commandFunctions.setCommands(this);
        this.shellUsrEX = shellUsrEX;
        this.ftpNet = ftpNet;
        this.finalVars = finalVars;
        this.ftpIO = ftpIO;
    }

    @ShellMethod(value = "Initializes the csv file uploading process.",
            key = {"upload", "upl", "up", "up-fl", "upload-file"})
    public String uploadFile(@ShellOption({"-P", "--path"}) String path,
                             @ShellOption({"-R", "--upload-response", "--upload-response-to-ftp", "--ur-to-ftp"}) boolean uploadResponseToFtp,
                             @ShellOption(value = {"-F", "--force", "--force-upload"}) boolean forceUpload) {
        return shellUsrEX.getSuccessMessage(
                commandFunctions.upload(
                        commandFunctions.getDataRows(commandFunctions.readFile(path)),
                        uploadResponseToFtp,
                        forceUpload
                )
        );
    }

    /**
     * Connects to an FTP server.
     *
     * @param host The server host name
     * @param port The server port
     * @param name The user name
     * @param pass The password
     * @param saveDetails When true - saves the connection details
     * @param autoConnect When true - tries to load connection details from file
     * @return The result of the connection process
     */
    @ShellMethod(value = "Connects to an FTP server.",
            key = {"connect", "con", "cf", "con-ftp", "connect-to-ftp"})
    public String connectToFtp(@ShellOption(value = {"-H", "--host"}, defaultValue = "") String host,
                               @ShellOption(value = {"-P", "--port"}, defaultValue = "") String port,
                               @ShellOption(value = {"-N", "--name"}, defaultValue = "") String name,
                               @ShellOption(value = {"-PW", "--password"}, defaultValue = "") String pass,
                               @ShellOption(value = {"-S", "--save"}) boolean saveDetails,
                               @ShellOption(value = {"-A", "--auto"}) boolean autoConnect){
        if (autoConnect){
            try {
                return shellUsrEX.getSuccessMessage(ftpNet.connect(ftpIO.loadFromFile()) ?
                        "Connected!" : "Connection failed!");
            } catch (IOException e) {
                return shellUsrEX.getErrorMessage(e.toString());
            }
        }else{
            int portInt;
            try{
                portInt = Integer.parseInt(port);
            }catch (Exception ex){
                portInt = finalVars.DEFAULT_FTP_PORT;
            }
            try {
                return normalFtpConnection(host, portInt, name, pass, saveDetails);
            } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                return shellUsrEX.getErrorMessage(e.toString());
            }
        }
    }

    @ShellMethod(value = "Initializes the csv file validation process. (Checks the file validation without uploading it to the database...)",
            key = {"validate", "val", "vd", "va-fl", "validate-file"})
    public String validateFile(@ShellOption({"-P", "--path"}) String path) {
        return shellUsrEX.getSuccessMessage(commandFunctions.validate(commandFunctions.getDataRows(commandFunctions.readFile(path))));
    }

    @ShellMethod(value = "Asks for FTP login details to save for later usage.",
            key = {"save-ftp", "save", "sv", "sv-ftp", "save-ftp-login"})
    public String saveFtpLogin(@ShellOption({"-U", "--url"}) String url,
                               @ShellOption({"-P", "--port"}) int port,
                               @ShellOption({"-N", "--name"}) String name,
                               @ShellOption({"-PW", "--pass"}) String pass) {
        try {
            return shellUsrEX.getSuccessMessage(commandFunctions.saveFtp(url, port, name, pass));
        } catch (Exception e) {
            return shellUsrEX.getErrorMessage(e.toString());
        }
    }

    @ShellMethod(value = "Removes the saved FTP login details if it finds any.",
            key = {"remove-ftp", "remove", "rm", "rm-ftp", "remove-ftp-login"})
    public String removeFtpLogin(@ShellOption({"-P", "--proceed"}) boolean proceed) {
        if (proceed){
            commandFunctions.removeFtp();
        }else{
            return shellUsrEX.getInfoMessage("You did not proceed.");
        }
        return "";
    }

    private String normalFtpConnection(String host, int p, String name, String pass, boolean save) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        finalVars.getCipher().init(Cipher.ENCRYPT_MODE, finalVars.getcKey());
        try {
            ConnectionDetail connectionDetail = new ConnectionDetail(host, p, name,
                    new String(Base64.getEncoder()
                            .encode(finalVars.getCipher().doFinal(pass.getBytes()))));

            String saveStatus = "\n";
            if (save){
                saveStatus += ftpIO.saveFtp(connectionDetail);
            }

            return shellUsrEX.getSuccessMessage(ftpNet.connect(connectionDetail) ?
                    "Connected!" + (saveStatus.equals("\n") ? "" : saveStatus) :
                    "Connection failed!" + (saveStatus.equals("\n") ? "" : saveStatus));
        } catch (IOException e) {
            return shellUsrEX.getErrorMessage(e.toString());
        }
    }

    @ShellMethod(value = "Attempts to disconnect from the FTP server (if connected to any)",
            key = {"disconnect", "dc", "d-connect", "disconnect-from-ftp"})
    public String disconnectFromFtp(@ShellOption({"-Y", "--yes"}) boolean sure) {
        if (sure){
            try {
                ftpNet.disconnect();
                return shellUsrEX.getSuccessMessage("Disconnected!");
            } catch (IOException e) {
                return shellUsrEX.getInfoMessage("Disconnect failed!");
            }
        }
        return shellUsrEX.getInfoMessage("If you really want to disconnect use the \"-Y\" switch!");
    }

}
