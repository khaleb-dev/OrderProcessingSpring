package mdev.OrderProcessingSpring.config;

import mdev.OrderProcessingSpring.functions.CommandFunctions;
import mdev.OrderProcessingSpring.shell.ShellUsrEX;
import org.jline.terminal.Terminal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ShellConfig {

    @Bean
    public ShellUsrEX shellUX(@Lazy Terminal terminal) {
        return new ShellUsrEX(terminal);
    }

    @Bean
    public CommandFunctions commandFunctions() {
        return new CommandFunctions();
    }

}
