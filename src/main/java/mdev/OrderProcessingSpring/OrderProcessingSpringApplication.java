package mdev.OrderProcessingSpring;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.logging.Logger;

/**
 * @author markodevelopment (Mihálovics Márkó)
 * @// TODO: 2/5/20 Create the Spring version of OrderProcessing..
 */
@SpringBootApplication
public class OrderProcessingSpringApplication implements CommandLineRunner{

	private static final Logger log = (Logger) LoggerFactory.getLogger(OrderProcessingSpringApplication.class);

	@Autowired
	JdbcTemplate jdbc;

	public static void main(String[] args) {
		SpringApplication.run(OrderProcessingSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
