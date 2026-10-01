package kz.iitu.springlab;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootTest
class SpringLab01ApplicationTests {

	@Test
	void contextLoads() {
	}

	@SpringBootApplication
	@ConfigurationPropertiesScan
	public class SpringLab01Application {

		public static void main(String[] args) {
			SpringApplication.run(SpringLab01Application.class, args);
		}
	}

}
