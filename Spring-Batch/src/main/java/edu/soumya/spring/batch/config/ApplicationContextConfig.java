package edu.soumya.spring.batch.config;

import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.soumya.spring.batch.detail.report.DiffDetailHtmlReportGenerator;
import edu.soumya.spring.batch.formatting.config.FormattingConfiguration;



/**
 * @author soumyab
 *
 */
@Configuration
public class ApplicationContextConfig {
	
	@Autowired
	private Environment env;
	
	@Qualifier("dataSource")
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.batch")
	public DataSource getBatchDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Qualifier("productDataSource")
	@Bean
	@ConfigurationProperties(prefix="poc.datasource")
	public DataSource getDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Qualifier("productJdbcTemplate")
	@Bean
	public JdbcTemplate productJdbcTemplate(@Qualifier("productDataSource") DataSource productDataSource){
		return new JdbcTemplate(productDataSource);
	}
	
	@Qualifier("sqlListFromDB")
	@Bean
	@JobScope
	public Set<String> getSqlListFromDB(){
		return new HashSet<String>();
	}
	
	@Qualifier("sqlListFromDirectory")
	@Bean
	@JobScope
	public Set<String> getSqlListFromDirectory(){
		return new HashSet<String>();
	}
	
	@Qualifier("allSqlIdSet")
	@Bean
	@JobScope
	public Set<String> getAllSqlId(){
		return new HashSet<String>();
	}
	
	@Bean
	public FormattingConfiguration getFormatConfig(Environment env) {
		return new FormattingConfiguration(env);
	}
	
	@Bean
	public DiffDetailHtmlReportGenerator getDetailDiffGenerator(Environment env) {
		return new DiffDetailHtmlReportGenerator(env);
	}
}