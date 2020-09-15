package com.tz.fdfs;

import com.tz.fdfs.controller.FileController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author tz
 */
@SpringBootApplication
public class SeaWeeDfsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(SeaWeeDfsApplication.class, args);
	}

}
