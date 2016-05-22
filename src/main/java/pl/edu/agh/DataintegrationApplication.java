package pl.edu.agh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DataintegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataintegrationApplication.class, args);
	}
}
