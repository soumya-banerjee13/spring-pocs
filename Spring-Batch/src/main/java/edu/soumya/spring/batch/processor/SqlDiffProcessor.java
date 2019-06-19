package edu.soumya.spring.batch.processor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import edu.soumya.spring.batch.constants.Constants;
import edu.soumya.spring.batch.detail.report.DiffDetailHtmlReportGenerator;
import edu.soumya.spring.batch.formatting.config.FormattingConfiguration;
import edu.soumya.spring.batch.model.SqlIssue;
import edu.soumya.spring.batch.model.SqlDBInfoModel;
import edu.soumya.spring.batch.util.StringFormatterUtils;



/**
 * @author soumyab
 *
 */
public class SqlDiffProcessor implements ItemProcessor<SqlIssue, SqlIssue>{
	
	private Environment env;
	
	@Autowired
	DiffDetailHtmlReportGenerator diffDetailReportGenerator;
	
	@Autowired
	FormattingConfiguration formatConfig;
	
	@Qualifier("productJdbcTemplate")
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Qualifier("sqlListFromDB")
	@Autowired
	Set<String> sqlListFromDB;
	
	@Qualifier("sqlListFromDirectory")
	@Autowired
	Set<String> sqlListFromDirectory;
	
	/**
	 * Query to Fetch Report Name and SQL from DB using SQL Id
	 */
	private static final String QUERY_FETCH_SQL = "select sql_name,sql from UI_GENER_QUERY where sql_id = ? ";
	
	public SqlDiffProcessor(Environment env) {
		this.env = env;
	}
	
	@Override
	public SqlIssue process(SqlIssue item) throws Exception {
		setDiffFlagAndGenerateDetailDiff(item);
		return item;
	}
	
	/**
	 * Generates Diff Detail Report of SQL from DB and Directory for an SQL
	 * 
	 * @param sqlIssue SQL Issue
	 * @throws IOException
	 */
	private void setDiffFlagAndGenerateDetailDiff(SqlIssue sqlIssue) throws IOException {
		if (!(sqlListFromDB.contains(sqlIssue.getSqlId()) && sqlListFromDirectory
				.contains(sqlIssue.getSqlId()))) {
			if(!sqlListFromDB.contains(sqlIssue.getSqlId())){
				sqlIssue.setSqlComment(Constants.ISSUE_NOT_FOUND_DB);
			} else {
				sqlIssue.setSqlName(getSqlAndNameFromDB(sqlIssue.getSqlId()).getSqlName());
				sqlIssue.setSqlComment(Constants.ISSUE_NOT_FOUND_DIR);
			}
			sqlIssue.setDiffExists(Boolean.TRUE);
			return;
		}else{
			String dirString = StringFormatterUtils.configurationSpecificModification(getSqlFromDirectory(sqlIssue.getSqlId()), formatConfig);
			SqlDBInfoModel sqlDBInfoModel = getSqlAndNameFromDB(sqlIssue.getSqlId());
			sqlIssue.setSqlName(sqlDBInfoModel.getSqlName());
			String dbString = StringFormatterUtils.configurationSpecificModification(sqlDBInfoModel.getSqlFromDB(),formatConfig);
			if(!StringUtils.equals(dbString, dirString)){
				sqlIssue.setDiffExists(Boolean.TRUE);
				diffDetailReportGenerator.generateDiffReport(dirString, dbString, sqlIssue.getSqlId());
				return;
			}
		}
		sqlIssue.setDiffExists(Boolean.FALSE);
	}
	
	/**
	 * Gets Sql and Name of SQL from DB
	 * 
	 * @param issueId SQL Issue Id
	 * @return sql from DB
	 * @throws IOException
	 */
	private SqlDBInfoModel getSqlAndNameFromDB(String issueId) {
		LobHandler lobHandler = new DefaultLobHandler();
		SqlDBInfoModel sqlDBInfoModel = (SqlDBInfoModel)jdbcTemplate.queryForObject(QUERY_FETCH_SQL,new Object[]{issueId},new RowMapper<SqlDBInfoModel>(){

			@Override
			public SqlDBInfoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				byte[] sqlData = lobHandler.getBlobAsBytes(rs, "SQL");
				SqlDBInfoModel sqlDBInfo = new SqlDBInfoModel();
				sqlDBInfo.setIssueId(issueId);
				sqlDBInfo.setSqlName(rs.getString("SQL_NAME"));
				sqlDBInfo.setSqlFromDB(StringFormatterUtils.replaceNewLineOSDependency(new String(sqlData)));
				return sqlDBInfo;
			}
			
		});
		return sqlDBInfoModel;
	}
	
	/**
	 * Gets Sql of SQL from Directory
	 * 
	 * @param issueId SQL Issue Id
	 * @return sql from Directory
	 * @throws IOException
	 */
	private String getSqlFromDirectory(String issueId) throws IOException {
		String fileNameWithExt = StringUtils.join(issueId,Constants.SQL_EXTENSION);
		Path sqlFolderFullPath = Paths.get(env.getProperty("product.repo.folder.path"),Constants.SQL_DIRECTORY_PATH,fileNameWithExt);
		byte[] sqlData = IOUtils.toByteArray(sqlFolderFullPath.toUri());
		return StringFormatterUtils.replaceNewLineOSDependency(new String(sqlData));
	}
}
