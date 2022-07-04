package com.example.TradeBoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradeBootApplication {

	static final Logger log =
			LoggerFactory.getLogger(TradeBootApplication.class);

	public static void main(String[] args) {

		log.info("Before Starting application");
		SpringApplication.run(TradeBootApplication.class, args);
		log.debug("Starting my application in debug with {} args", args.length);
	}

}
