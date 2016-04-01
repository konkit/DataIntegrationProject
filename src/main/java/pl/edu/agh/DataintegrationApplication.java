package pl.edu.agh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.services.TwitterService;

@SpringBootApplication
public class DataintegrationApplication implements CommandLineRunner {

    @Autowired
    private TwitterService twitterService;

	public static void main(String[] args) {
		SpringApplication.run(DataintegrationApplication.class, args);
	}

    public void run(String[] args) {
        twitterService.start();
    }
}
