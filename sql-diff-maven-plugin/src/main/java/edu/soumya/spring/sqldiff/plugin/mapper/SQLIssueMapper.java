package edu.soumya.spring.sqldiff.plugin.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.soumya.spring.sqldiff.plugin.model.SQLIssue;

/**
 * @author soumyab
 *
 */
public class SQLIssueMapper implements RowMapper<SQLIssue>{
	@Override
	public SQLIssue mapRow(ResultSet rs, int rowNum) throws SQLException {
		SQLIssue sqlIssue = new SQLIssue();
		sqlIssue.setSqlId(rs.getString("SQL_ID"));
		return sqlIssue;
	}
}