package edu.soumya.spring.sqldiff.plugin.model;

/**
 * @author soumyab
 *
 */
public class SqlDBInfoModel {
	private String issueId;
	private String sqlName;
	private String sqlFromDB;
	
	/**
	 * @return the issueId
	 */
	public String getIssueId() {
		return issueId;
	}

	/**
	 * @param issueId the issueId to set
	 */
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	
	/**
	 * @return the sqlName
	 */
	public String getSqlName() {
		return sqlName;
	}

	/**
	 * @param sqlName the sqlName to set
	 */
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	/**
	 * @return the sqlFromDB
	 */
	public String getSqlFromDB() {
		return sqlFromDB;
	}

	/**
	 * @param sqlFromDB the sqlFromDB to set
	 */
	public void setSqlFromDB(String sqlFromDB) {
		this.sqlFromDB = sqlFromDB;
	}
	
}
