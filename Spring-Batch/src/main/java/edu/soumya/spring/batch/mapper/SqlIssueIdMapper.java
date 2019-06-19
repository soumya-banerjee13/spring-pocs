package edu.soumya.spring.batch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.soumya.spring.batch.model.SqlIssue;

/**
 * @author soumyab
 *
 */
public class SqlIssueIdMapper implements RowMapper<SqlIssue>{
	@Override
	public SqlIssue mapRow(ResultSet rs, int rowNum) throws SQLException {
		SqlIssue sqlIssue = new SqlIssue();
		sqlIssue.setSqlId(rs.getString("SQL_ID"));
		return sqlIssue;
	}
}
