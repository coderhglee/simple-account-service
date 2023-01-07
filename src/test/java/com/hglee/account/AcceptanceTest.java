package com.hglee.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.config.EncoderConfig;
import io.restassured.config.LogConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void beforeAll() {
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
	}

	@AfterEach
	public void reset() {
		RestAssuredMockMvc.reset();
	}

	protected MockMvcRequestSpecification given() {
		return RestAssuredMockMvc.given()
				.config(RestAssuredMockMvcConfig.config()
						.encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
				.config(RestAssuredMockMvcConfig.config()
						.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
				.log()
				.all(true);
	}
}
