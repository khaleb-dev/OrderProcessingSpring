package mdev.orderProcessingSpring.utils;

import ch.qos.logback.classic.Logger;
import mdev.orderProcessingSpring.shell.Commands;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class CSVReader {

    private Logger logger;

    @PostConstruct
    public void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(CSVReader.class);
    }

    private Commands commands;

    public Order[] readOrdersFromFile(File file) {
        List<Order> orderList = new ArrayList<>();
        try (ICsvBeanReader reader = new CsvBeanReader(new FileReader(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {
            reader.getHeader(true);
            final String[] headers = new String[]{
                    "LineNumber", null, "OrderId", "BuyerName",
                    "BuyerEmail", "Address", "Postcode", null, null, null, null, "OrderDate"};
            final CellProcessor[] processors = getProcessors();
            Order csvBean;

            while ((csvBean = reader.read(Order.class, headers, processors)) != null) {
                csvBean.setShellUsrEX(commands.shellUsrEX);
                orderList.add(csvBean);
            }

            reader.close();

            Order[] orders = new Order[orderList.size()];
            for (int i = 0; i < orders.length; i++){
                orders[i] = orderList.get(i);
            }

            return orders;
        }catch (Exception ex){
            logger.error(commands.shellUsrEX.getErrorMessage(ex.toString()));
            return null;
        }
    }

    public Item[] readItemsFromFile(File file) {
        List<Item> itemList = new ArrayList<>();
        try (ICsvBeanReader reader = new CsvBeanReader(new FileReader(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {
            reader.getHeader(true);
            final String[] headers = new String[]{
                    "LineNumber","OrderItemId","OrderId", null, null, null, null,
                    "SalePrice","ShippingPrice","SKU","Status", null};
            final CellProcessor[] processors = getProcessors();
            Item csvBean;

            while ((csvBean = reader.read(Item.class, headers, processors)) != null) {
                csvBean.setShellUsrEX(commands.shellUsrEX);
                itemList.add(csvBean);
            }

            reader.close();

            Item[] items = new Item[itemList.size()];
            for (int i = 0; i < items.length; i++){
                items[i] = itemList.get(i);
            }

            return items;
        }catch (Exception ex){
            logger.error(commands.shellUsrEX.getErrorMessage(ex.toString()));
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
