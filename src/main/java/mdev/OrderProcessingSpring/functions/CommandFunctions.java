package mdev.OrderProcessingSpring.functions;

import mdev.OrderProcessingSpring.OPSpringApp;
import mdev.OrderProcessingSpring.shell.Commands;
import mdev.OrderProcessingSpring.utils.CSVReader;
import mdev.OrderProcessingSpring.utils.DataRow;
import mdev.OrderProcessingSpring.utils.FinalVars;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CommandFunctions {

    private Commands commands;

    @Autowired
    public FinalVars finalVars;

    @Autowired
    public CSVReader csvReader;

    public File readFile(String filePath){
        return new File(filePath);
    }

    public DataRow[] getDataRows(File file){
        return csvReader.readFile(file);
    }

    /**
     * @// TODO: 2/7/20 Percentage calc, Question to upload not 100% fine file, Upload, Return process results
     */
    public String upload(DataRow[] dataRows){

        return null;
    }

    /**
     * @// TODO: 2/7/20 Percentage calc, Return results
     */
    public String validate(DataRow[] dataRows){
        return null;
    }

    public String saveFtp(String url, int port, String name, String pass){
        Properties p = new Properties();
        p.put(finalVars.FTP_SERVER_KEY, url);
        p.put(finalVars.FTP_PORT_KEY, port + "");
        p.put(finalVars.FTP_USER_KEY, name);
        p.put(finalVars.FTP_PASS_KEY, finalVars.getEncrypt().encode(pass));

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
}
