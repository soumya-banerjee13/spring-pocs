package edu.soumya.spring.batch.reader;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import edu.soumya.spring.batch.mapper.SqlIssueIdMapper;
import edu.soumya.spring.batch.model.SqlIssue;

/**
 * Reads All SQL Id from DB
 * 
 * @author soumyab
 *
 */
public class SqlIdFromDBReader extends JdbcCursorItemReader<SqlIssue> {
	public SqlIdFromDBReader(){}
	/**
	 * Query to fetch all SQL id from DB
	 */
	private static final String QUERY_FETCH_SQL_ID = "select sql_id,sql_name from UI_GENER_QUERY";
	public ItemReader<SqlIssue> fetchSqlIdList(DataSource dataSource){
		this.setDataSource(dataSource);
		this.setSql(QUERY_FETCH_SQL_ID);
		this.setRowMapper(new SqlIssueIdMapper());
		return this;
	}
}
