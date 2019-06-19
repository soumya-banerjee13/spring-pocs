package edu.soumya.spring.sqldiff.plugin.constants;

/**
 * @author soumyab
 *
 */
public class Constants {
	public static final String FILE_LOC_CONFIG_NAME = "fileLocation";
	public static final String DATASOURCE_CONFIG_NAME = "dataSource";
	public static final String LOGBACK_CONFIG_NAME = "logback";
	public static final String SQL_FORMAT_CONFIG_NAME = "sqlFormatConfig";
	
	public static final String FILE_LOC_CONFIG_FILE_NAME = "file_location.properties";
	public static final String DATASOURCE_CONFIG_FILE_NAME = "datasource-cfg.properties";
	public static final String LOGBACK_CONFIG_FILE_NAME = "logback.properties";
	public static final String SQL_FORMAT_CONFIG_FILE_NAME = "sql-format-config.properties";
	
	public static final String SQL_DIRECTORY_PATH = "soumyab/develop/abc-deployments/Queries";
	
	public static final String COMMA_SEPERATOR = ",";
	public static final String NEW_LINE = "\n";
	public static final String BLANK_SPACE_AS_PATTERN = "_BLANK_SPACE_";
	public static final String COMMA_AS_PATTERN = "_COMMA_";
	
	public static final String SQL_EXTENSION = ".sql";
	public static final String HTML_EXTENSION = ".html";
	
	public static final String ISSUE_NOT_FOUND_DB = "SQL Not Found in DB";
	public static final String ISSUE_NOT_FOUND_DIR = "SQL Not Found in Directory";
	
	public static final String DIFF_DETAIL_FOLDER_NAME = "SQL_diff_details";
	public static final String DIFF_DETAIL_FILE_SUFFIX = "_dir_vs_db";
	
	public static final String SQL_IN_FILE_TITLE = "Sql in File";
	public static final String SQL_IN_DB_TITLE = "Sql in DB";
	public static final String SQL_ID_HEADER = "SQL ID";
	public static final String SQL_NAME_HEADER = "SQL Name";
	public static final String COMMENT_HEADER = "Comment";
	public static final String TEXT_MISSING_FILE = "Missing in File";
	public static final String TEXT_MISSING_DB = "Missing in DB";
	public static final String DIFF_DETAIL_DESC = "N.B:- Detailed Diff Reports are shown in the following way:"+ 
												NEW_LINE +
												"1. Common part of Query String shown as normal text, "+
												NEW_LINE +
												"2. Part of Query String found in DB but not in file shown inside the tag &lt;" + TEXT_MISSING_FILE + "&gt; &lt;/" + TEXT_MISSING_FILE + "&gt;,"+
												NEW_LINE +
												"3. Part of Query String found in file but not in DB shown inside the tag &lt;" + TEXT_MISSING_DB + "&gt; &lt;/"+ TEXT_MISSING_DB +"&gt;";
}
