package fabbitox.flow.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JdbcConfig {
	private final DataSource dataSource;

    @Bean
    JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}
}
