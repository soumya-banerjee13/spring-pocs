package edu.soumya.spring.sqldiff.plugin.runner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.soumya.spring.sqldiff.plugin.constants.Constants;
import edu.soumya.spring.sqldiff.plugin.diff.processor.SqlDiffProcessor;
import edu.soumya.spring.sqldiff.plugin.model.SQLIssue;
import edu.soumya.spring.sqldiff.plugin.report.creator.DiffSummaryReportCreator;

/**
 * @author soumyab
 *
 */
public class SqlDiffRunner {
	
	@Autowired
	Environment env;
	
	@Qualifier("productJdbcTemplate")
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	SqlDiffProcessor diffProcessor;
	
	@Autowired
	DiffSummaryReportCreator diffReportCreator;
	
	private static Logger LOGGER = LoggerFactory.getLogger(SqlDiffRunner.class);
	
	private Set<String> sqlListFromDB;
	private Set<String> sqlListFromDirectory;
	private Set<String> allSqlIdSet;
	
	/**
	 * Query to fetch all SQL id from DB
	 */
	private static final String QUERY_FETCH_SQL_ID = "select sql_id,sql_name from UI_GENER_QUERY";
	public SqlDiffRunner() {
		sqlListFromDB = new HashSet<String>();
		sqlListFromDirectory = new HashSet<String>();
		allSqlIdSet = new HashSet<String>();
	}
	public void execute() throws IOException, MojoExecutionException {
		readSqlNamesFromDB();
		readSqlNamesFromDirectory();
		createDiffReport();
	}
	
	private void readSqlNamesFromDB() {
		List<SQLIssue> issueList = jdbcTemplate.query(QUERY_FETCH_SQL_ID, new RowMapper<SQLIssue>(){
			@Override
			public SQLIssue mapRow(ResultSet rs, int rowNum) throws SQLException {
				SQLIssue sqlIssue = new SQLIssue();
				sqlIssue.setSqlId(rs.getString("SQL_ID"));
				return sqlIssue;
			}
		});
		
		for(SQLIssue sqlIssue : issueList) {
			LOGGER.debug("Fetched sql id from Database:" + sqlIssue.getSqlId());
			sqlListFromDB.add(sqlIssue.getSqlId());
			allSqlIdSet.add(sqlIssue.getSqlId());
		}
	}
	
	private void readSqlNamesFromDirectory() {
		Path sqlFolderFullPath = Paths.get(env.getProperty("poc.repo.folder.path"),Constants.SQL_DIRECTORY_PATH); 
		LOGGER.info("Fetching all checked in SQL files from the directory path: " + sqlFolderFullPath.toString());	
		File sqlFolder = new File(sqlFolderFullPath.toUri());
		File[] sqlFiles = sqlFolder.listFiles();
		for(File sqlFile:sqlFiles) {
			if(sqlFile.isFile()){
				if(sqlFile.getName().indexOf(".")!=-1){
					sqlListFromDirectory.add(sqlFile.getName().substring(0,sqlFile.getName().indexOf(".")));
					allSqlIdSet.add(sqlFile.getName().substring(0,sqlFile.getName().indexOf(".")));
				}else{
					sqlListFromDirectory.add(sqlFile.getName());
					allSqlIdSet.add(sqlFile.getName());
				}
				LOGGER.debug("Fetched sql id from Product Repo:" + sqlFile.getName());
			}
		}
	}
	
	private void createDiffReport() throws IOException, MojoExecutionException {
		List<SQLIssue> sqlIssueList = new LinkedList<>();
		for(String issueId : allSqlIdSet) {
			sqlIssueList.add(diffProcessor.generateDetailDiff(issueId,sqlListFromDB,sqlListFromDirectory));
		}
		diffReportCreator.createReport(sqlIssueList);
		for(SQLIssue sqlIssue:sqlIssueList) {
			if (sqlIssue.getDiffExists())
				throw new MojoExecutionException(
						"Found diff between query in File and Database for 1 or more SQL."
								+ Constants.NEW_LINE
								+ "Please find Diff Report in the location: "
								+ env.getProperty("sql.diff.dest.path"));
		}
		LOGGER.info("Found no SQL Diff. "+ Constants.NEW_LINE +"Please find Diff Report in the location: "
								+ env.getProperty("sql.diff.dest.path"));
	}
}
