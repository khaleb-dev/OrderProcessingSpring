package mdev.OrderProcessingSpring.utils;

import mdev.OrderProcessingSpring.OPSpringApp;
import mdev.OrderProcessingSpring.shell.Commands;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVReader {

    private Commands commands;

    public DataRow[] readFile(File file) {
        List<DataRow> dataList = new ArrayList<>();
        try (ICsvBeanReader reader = new CsvBeanReader(new FileReader(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {
            final String[] headers = reader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            DataRow csvBean;
            OPSpringApp.log.debug(commands.shellUsrEX.getInfoMessage("LineNumber;OrderItemId;OrderId;BuyerName;BuyerEmail;Address;Postcode;SalePrice;ShippingPrice;SKU;Status;OrderDate"));
            while ((csvBean = reader.read(DataRow.class, headers, processors)) != null) {
                csvBean.setShellUsrEX(commands.shellUsrEX);
                OPSpringApp.log.debug(commands.shellUsrEX.getInfoMessage(
                                csvBean.getLineNumber() + ";" +
                                csvBean.getOrderItemId() + ";" +
                                csvBean.getOrderId() + ";" +
                                csvBean.getBuyerName() + ";" +
                                csvBean.getBuyerEmail() + ";" +
                                csvBean.getAddress() + ";" +
                                csvBean.getPostcode() + ";" +
                                csvBean.getSalePrice() + ";" +
                                csvBean.getShippingPrice() + ";" +
                                csvBean.getSKU() + ";" +
                                csvBean.getStatus() + ";" +
                                csvBean.getOrderDate()));
                dataList.add(csvBean);
            }

            reader.close();

            DataRow[] dr = new DataRow[dataList.size()];
            for (int i = 0; i < dr.length; i++){
                dr[i] = dataList.get(i);
            }

            return dr;
        }catch (Exception ex){
            OPSpringApp.log.error(commands.shellUsrEX.getErrorMessage(ex.toString()));
            return null;
        }
    }

    private CellProcessor[] getProcessors(){

        return new CellProcessor[] {
                new ConvertNullTo(""), // LineNumber
                new ConvertNullTo(""), // OrderId
                new ConvertNullTo(""), // OrderItemId
                new ConvertNullTo(""), // BuyerName
                new ConvertNullTo(""), // BuyerEmail
                new ConvertNullTo(""), // Address
                new ConvertNullTo(""), // Postcode
                new ConvertNullTo(""), // SalePrice
                new ConvertNullTo(""), // ShippingPrice
                new ConvertNullTo(""), // SKU
                new ConvertNullTo(""), // Status
                new ConvertNullTo(""), // OrderDate
        };
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }
}
