package edu.soumya.spring.batch.config;

import java.util.Set;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import edu.soumya.spring.batch.model.SqlIssue;
import edu.soumya.spring.batch.processor.SqlDiffProcessor;
import edu.soumya.spring.batch.reader.AllSqlIdReader;
import edu.soumya.spring.batch.reader.SqlIdFromDBReader;
import edu.soumya.spring.batch.reader.SqlIdFromDirectoryReader;
import edu.soumya.spring.batch.writer.SqlDiffReportWriter;
import edu.soumya.spring.batch.writer.SqlIdFromDdToListWriter;
import edu.soumya.spring.batch.writer.SqlIdFromDirectoryToListWriter;

/**
 * @author soumyab
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	// The Environment class serves as the property holder
	// and stores all the properties loaded by the @PropertySource
	@Autowired
	private Environment env;
	
	@Qualifier("productDataSource")
	@Autowired
	DataSource productDataSource;
	
	@Qualifier("allSqlIdSet")
	@Autowired
	Set<String> allSqlIdSet;
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
    public StepBuilderFactory stepBuilderFactory;
	
	//Readers
	@Bean(destroyMethod="")
	public SqlIdFromDBReader sqlIdFromDBReader() {
		SqlIdFromDBReader sqlIdFromDBReader = new SqlIdFromDBReader();
		sqlIdFromDBReader.fetchSqlIdList(productDataSource);
		return sqlIdFromDBReader;
	}
	
	@Bean
	public SqlIdFromDirectoryReader sqlIdFromDirectoryReader() {
		return new SqlIdFromDirectoryReader(env);
	}
	
	@Bean
	public AllSqlIdReader allSqlIdReader() {
		return new AllSqlIdReader(allSqlIdSet);
	}
	
	//Processors
	@Bean
	public SqlDiffProcessor sqlDiffProcessor(){
		return new SqlDiffProcessor(env);
	}
	
	//Writers
	@Bean
	public SqlIdFromDdToListWriter sqlIdFromDdToListWriter() {
		return new SqlIdFromDdToListWriter();
	}
	
	@Bean
	public SqlIdFromDirectoryToListWriter sqlIdFromDirToListWriter() {
		return new SqlIdFromDirectoryToListWriter();
	}
	
	@Bean
	public SqlDiffReportWriter sqlDiffReportWriter() {
		return new SqlDiffReportWriter(env);
	}
	
	//Job
	@Bean
    public Job sqlComparisonJob(@Qualifier("fetchSqlIDFromDB") Step step1,@Qualifier("fetchSqlIdFromDir") Step step2,
    		@Qualifier("generateDiffReport") Step step3) {
        return jobBuilderFactory.get("sqlComparisonJob")
            .incrementer(new RunIdIncrementer())
            .start(step1)
            .next(step2)
            .next(step3)
            .build();
    }
	
	//Steps
	@Qualifier("fetchSqlIDFromDB")
	@Bean
	public Step fetchSqlIDFromDB(SqlIdFromDBReader reader,SqlIdFromDdToListWriter writer) {
		return stepBuilderFactory.get("fetchSqlIDFromDB")
				.<SqlIssue, SqlIssue>chunk(20)
				.reader(reader)
				.writer(writer)
				.build();
	}
	
	@Qualifier("fetchSqlIdFromDir")
	@Bean
	public Step fetchSqlIdFromDirectory(SqlIdFromDirectoryReader reader,SqlIdFromDirectoryToListWriter writer) {
		return stepBuilderFactory.get("fetchSqlIdFromDir")
				.<SqlIssue,SqlIssue>chunk(20)
				.reader(reader)
				.writer(writer)
				.build();
	}
	
	@Qualifier("generateDiffReport")
	@Bean
	public Step generateDiffReport(AllSqlIdReader reader,SqlDiffProcessor processor,SqlDiffReportWriter writer){
		return stepBuilderFactory.get("generateDiffReport")
				.<SqlIssue,SqlIssue>chunk(20)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}
}
