package mdev.orderProcessingSpring.shell;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public class ShellUsrEX {

    @Value("${shell.ux.design.info}")
    private String infoColor;

    @Value("${shell.ux.design.success}")
    private String successColor;

    @Value("${shell.ux.design.warning}")
    private String warningColor;

    @Value("${shell.ux.design.error}")
    private String errorColor;

    public String getColored(String message, ShellUXDesign color) {
        return (new AttributedStringBuilder()).append(message, AttributedStyle.DEFAULT.foreground(color.getValue())).toAnsi();
    }

    public String getInfoMessage(String message) {
        return getColored(message, ShellUXDesign.valueOf(infoColor));
    }

    public String getSuccessMessage(String message) {
        return getColored(message, ShellUXDesign.valueOf(successColor));
    }

    public String getWarningMessage(String message) {
        return getColored(message, ShellUXDesign.valueOf(warningColor));
    }

    public String getErrorMessage(String message) {
        return getColored(message, ShellUXDesign.valueOf(errorColor));
    }

}
