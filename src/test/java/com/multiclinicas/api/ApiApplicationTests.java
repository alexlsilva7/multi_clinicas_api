package com.multiclinicas.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ApiApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {
		assertNotNull(dataSource);
	}

	@Test
	void testH2DatabaseConnection() throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			assertNotNull(connection);
			assertTrue(connection.isValid(1));

			// Verifica se está usando H2
			String databaseProductName = connection.getMetaData().getDatabaseProductName();
			assertEquals("H2", databaseProductName);
		}
	}

}
