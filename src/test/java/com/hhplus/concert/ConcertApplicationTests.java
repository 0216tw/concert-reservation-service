package com.hhplus.concert;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class ConcertApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeAll
	public static void setup(@Autowired JdbcTemplate jdbcTemplate) throws URISyntaxException, IOException {
		String schemaSql = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("schema.sql").toURI())));
		String dataSql = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("data.sql").toURI())));
		jdbcTemplate.execute(schemaSql);
		jdbcTemplate.execute(dataSql);
	}
	@Test
	void contextLoads() {
	}


}
