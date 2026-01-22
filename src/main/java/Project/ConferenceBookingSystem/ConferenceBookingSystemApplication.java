package Project.ConferenceBookingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ConferenceBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceBookingSystemApplication.class, args);
	}

}
