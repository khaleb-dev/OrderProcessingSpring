package mdev.OrderProcessingSpring.functions.processing;

import ch.qos.logback.classic.Logger;
import mdev.OrderProcessingSpring.shell.Commands;
import mdev.OrderProcessingSpring.shell.ShellUsrEX;
import mdev.OrderProcessingSpring.utils.IdDAO;
import mdev.OrderProcessingSpring.utils.Order;
import mdev.OrderProcessingSpring.utils.ValidationError;
import mdev.OrderProcessingSpring.utils.vars.DataBaseVars;
import mdev.OrderProcessingSpring.utils.vars.StatusCodes;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author markodevelopment (Mihálovics Márkó)
 *
 * The class where the file validation happens according to the task requirements..
 *
 */
@Component
public class Validator {

    private Logger logger;

    @PostConstruct
    public void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(Validator.class);
    }

    @Autowired
    public StatusCodes statusCodes;

    @Autowired
    public DataBaseVars dataBaseVars;

    @Autowired
    public ShellUsrEX shellUsrEX;

    @Autowired
    public Commands commands;

    @Autowired
    public ApplicationContext context;

    private IdDAO idDAO;

    @Autowired
    public ErrorMessageCreator errorMessageCreator;

    @Autowired
    public PercentageCalculator percentageCalculator;

    private ArrayList<ValidationError> validationErrors;
    private boolean valid; // The validity of the validated file
    private boolean email, fill, date, shippingPrice, salePrice,
            status, orderItemId, orderId, postcode, lineNumber;

    private ArrayList<Order> validData;

    public String validate(Order[] orders, boolean ignoreOutput){
        init();

        for (Order order : orders){
            order.disableWarn();
            check(order);
        }

        if (!ignoreOutput){
            if (valid){
                return shellUsrEX.getSuccessMessage(
                                "\n-----------------------" +
                                "\nThe file is 100% valid!"+
                                "\n-----------------------");
            }

            return shellUsrEX.getWarningMessage(Errors(orders.length));
        }

        return "";
    }

    private void init(){
        validData = new ArrayList<>();
        validationErrors = new ArrayList<>();
        valid = true;
        idDAO = context.getBean(IdDAO.class);
    }

    private void check(Order order){
        setBools(order);

        if (checkEmpty(order.getOrderDate())){
            date = validDate(dataBaseVars.DATE_FORMAT, order.getOrderDate(), Locale.ENGLISH);
        }else{
            date = true;
        }

        if (!email || !fill || !date || !shippingPrice || !salePrice ||
                !status || !orderItemId || !orderId || !postcode || !lineNumber){
            valid = false;
            String errorMessage = errorMessageCreator.create(email, fill, date, shippingPrice,
                    salePrice, status, orderItemId, orderId, order);
            validationErrors.add(new ValidationError(order.getLineNumber(),
                    errorMessage, statusCodes.STATUS_ERROR));
        }else{
            validData.add(order);
        }
    }

    private void setBools(Order order){
        email = validEmail(order.getBuyerEmail());
        fill = validFill(order.getBuyerEmail(), order.getBuyerName(),
                order.getSKU(), order.getStatus(), order.getAddress());
        date = true;
        shippingPrice = validShippingPrice(order.getShippingPrice());
        salePrice = validSalePrice(order.getSalePrice());
        status = validStatus(order.getStatus());
        orderItemId = !validOrderItemIdInUse(order.getOrderItemId());
        orderId = !validOrderIdInUse(order.getOrderId());
        postcode = order.getPostcode() != -1;
        lineNumber = order.getLineNumber() != -1;
    }

    private boolean checkEmpty(String s){
        if (s == null){
            return true;
        }
        return !s.isEmpty();
    }

    private String Errors(int orderSize){
        StringBuilder builder = new StringBuilder();

        builder.append("\n--------------------");
        builder.append("\nThe file is invalid!");
        builder.append("\n--------------------");

        float percent = percentageCalculator.calc(orderSize, validationErrors);
        if (percent != 0.0f){
            builder.append("\nBut ");
            builder.append(percent);
            builder.append("% of it is valid...\n");
        }

        for (ValidationError ve : validationErrors){
            builder.append(ve.getMessage());
        }

        return builder.toString();
    }

    private boolean validEmail(String email){
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            logger.debug(commands.shellUsrEX.getInfoMessage(ex.toString()));
            result = false;
        }
        return result;
    }

    private boolean validFill(String... data){
        boolean correct = true;
        for (String s : data){
            correct = correct && checkEmpty(s);
        }
        return correct;
    }

    private boolean validDate(@SuppressWarnings("SameParameterValue") String format,
                              String value, @SuppressWarnings("SameParameterValue") Locale locale) {
        LocalDateTime dateTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            dateTime = LocalDateTime.parse(value, formatter);
            String result = dateTime.format(formatter);
            return result.equals(value);
        } catch (DateTimeParseException dtpe) {
            logger.debug(commands.shellUsrEX.getInfoMessage(dtpe.toString()));
            try {
                LocalDate localDate = LocalDate.parse(value, formatter);
                String result = localDate.format(formatter);
                return result.equals(value);
            } catch (DateTimeParseException dtpe1) {
                logger.debug(commands.shellUsrEX.getInfoMessage(dtpe1.toString()));
                try {
                    LocalTime localTime = LocalTime.parse(value, formatter);
                    String result = localTime.format(formatter);
                    return result.equals(value);
                } catch (DateTimeParseException dtpe2) {
                    logger.debug(commands.shellUsrEX.getInfoMessage(dtpe2.toString()));
                }
            }
        }

        return false;
    }

    private boolean validShippingPrice(float price){
        return price >= 0;
    }

    private boolean validSalePrice(float price){
        return price >= 1;
    }

    private boolean validStatus(String status){
        return status.equals(statusCodes.STATUS_IN) ||
                status.equals(statusCodes.STATUS_OUT);
    }

    private boolean validOrderItemIdInUse(int id) {
        return idDAO.validOrderItemId(id);
    }

    private boolean validOrderIdInUse(int id) {
        return idDAO.validOrderIdInUse(id);
    }

    public boolean isValid() {
        return valid;
    }

    public ArrayList<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public Order[] getValidData() {
        return validData.toArray(new Order[0]);
    }
}
