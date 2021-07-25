package SmartContactManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

public class SmartContactManagetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartContactManagetApplication.class, args);
	}

}
