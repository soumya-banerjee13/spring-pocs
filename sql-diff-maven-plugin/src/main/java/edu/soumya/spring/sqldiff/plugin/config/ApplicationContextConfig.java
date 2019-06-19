package edu.soumya.spring.sqldiff.plugin.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import edu.soumya.spring.sqldiff.plugin.diff.processor.SqlDiffProcessor;
import edu.soumya.spring.sqldiff.plugin.formatting.config.FormattingConfiguration;
import edu.soumya.spring.sqldiff.plugin.report.creator.DiffDetailHtmlReportCreator;
import edu.soumya.spring.sqldiff.plugin.report.creator.DiffSummaryReportCreator;
import edu.soumya.spring.sqldiff.plugin.runner.SqlDiffRunner;

/**
 * @author soumyab
 *
 */
@Configuration
public class ApplicationContextConfig {
	@Autowired
	private Environment env;

	@Qualifier("productDataSource")
	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		// See: datasouce-cfg.properties
		dataSource.setDriverClassName(env.getProperty("poc.datasource.driverClassName"));
		dataSource.setUrl(env.getProperty("poc.datasource.jdbcUrl"));
		dataSource.setUsername(env.getProperty("poc.datasource.username"));
		dataSource.setPassword(env.getProperty("poc.datasource.password"));
		return dataSource;

	}

	@Qualifier("productJdbcTemplate")
	@Bean
	public JdbcTemplate productJdbcTemplate(@Qualifier("productDataSource") DataSource productDataSource) {
		return new JdbcTemplate(productDataSource);
	}

	@Bean
	public SqlDiffRunner sqlDiffRunner() {
		return new SqlDiffRunner();
	}

	@Bean
	public SqlDiffProcessor sqlDiffProcessor() {
		return new SqlDiffProcessor(env);
	}
	
	@Bean
	public FormattingConfiguration getFormatConfig(Environment env) {
		return new FormattingConfiguration(env);
	}
	
	@Bean
	public DiffSummaryReportCreator diffReportCreator() {
		return new DiffSummaryReportCreator(env);
	}
	
	@Bean
	public DiffDetailHtmlReportCreator getDetailDiffGenerator(Environment env) {
		return new DiffDetailHtmlReportCreator(env);
	}
}
