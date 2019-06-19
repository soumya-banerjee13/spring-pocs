package edu.soumya.spring.sqldiff.plugin.model;

import org.apache.commons.lang3.StringUtils;

/**
 * @author soumyab
 *
 */
public class SQLIssue {
	private String sqlId;
	private String sqlName = StringUtils.EMPTY;
	private String sqlComment = StringUtils.EMPTY;
	private Boolean diffExists = Boolean.FALSE;
	
	/**
	 * @return the sqlId
	 */
	public String getSqlId() {
		return sqlId;
	}

	/**
	 * @param sqlId
	 */
	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
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
	 * @return the diffExists flag
	 */
	public Boolean getDiffExists() {
		return diffExists;
	}

	/**
	 * @param diffExists
	 */
	public void setDiffExists(Boolean diffExists) {
		this.diffExists = diffExists;
	}

	/**
	 * @return the sqlComment
	 */
	public String getSqlComment() {
		return sqlComment;
	}

	/**
	 * @param sqlComment the sqlComment to set
	 */
	public void setSqlComment(String sqlComment) {
		this.sqlComment = sqlComment;
	}
	
}

