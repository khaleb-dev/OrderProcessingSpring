package mdev.OrderProcessingSpring.shell;

import mdev.OrderProcessingSpring.functions.CommandFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class Commands {

    private final CommandFunctions commandFunctions;
    public final ShellUsrEX shellUsrEX;

    @Autowired
    public Commands(CommandFunctions commandFunctions, ShellUsrEX shellUsrEX) {
        this.commandFunctions = commandFunctions;
        this.shellUsrEX = shellUsrEX;
    }

    @ShellMethod(value = "Initializes the csv file uploading process.",
            key = {"upload", "upl", "up", "up-fl", "upload-file"})
    public String uploadFile(@ShellOption({"-P", "--path"}) String path) {
        return shellUsrEX.getSuccessMessage(commandFunctions.upload(commandFunctions.getDataRows(commandFunctions.readFile(path))));
    }

    @ShellMethod(value = "Initializes the csv file validation process. (Checks the file validation without uploading it to the database...)",
            key = {"validate", "val", "vd", "va-fl", "validate-file"})
    public String validateFile(@ShellOption({"-P", "--path"}) String path) {
        return shellUsrEX.getSuccessMessage(commandFunctions.validate(commandFunctions.getDataRows(commandFunctions.readFile(path))));
    }

    @ShellMethod(value = "Asks for FTP login details to save for later usage.",
            key = {"save-ftp", "save", "sv", "sv-ftp", "save-ftp-login"})
    public String saveFtpLogin(@ShellOption({"-U", "--url"}) String url,
                               @ShellOption({"-N", "--name"}) String name,
                               @ShellOption({"-P", "--pass"}) String pass) {
        return shellUsrEX.getSuccessMessage(commandFunctions.saveFtp(url, name, pass));
    }

    @ShellMethod(value = "Removes the saved FTP login details if it finds any.",
            key = {"remove-ftp", "remove", "rm", "rm-ftp", "remove-ftp-login"})
    public String removeFtpLogin(@ShellOption({"-P", "--proceed"}) boolean proceed) {
        if (proceed){
            commandFunctions.removeFtp();
            return shellUsrEX.getSuccessMessage("The FTP Login details have been removed.");
        }else{
            return shellUsrEX.getInfoMessage("You did not remove the FTP Login details.");
        }
    }

}
