package info.colinhan.sisyphus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SisyphusServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisyphusServerApplication.class, args);
	}

}
