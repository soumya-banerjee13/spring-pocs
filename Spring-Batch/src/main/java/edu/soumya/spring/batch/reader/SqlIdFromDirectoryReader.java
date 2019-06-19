package edu.soumya.spring.batch.reader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.env.Environment;

import edu.soumya.spring.batch.constants.Constants;
import edu.soumya.spring.batch.model.SqlIssue;


/**
 * Reads All SQL Id from Directory
 * 
 * @author soumyab
 *
 */
public class SqlIdFromDirectoryReader implements ItemReader<SqlIssue>{
	private static Logger LOGGER = LoggerFactory.getLogger(SqlIdFromDirectoryReader.class);
	
	private List<String> sqlIdListFromFolder;
	private Integer index = 0;
	
	public SqlIdFromDirectoryReader(Environment env){
		readAllSqlFileName(env);
	}
	
	/**
	 * Reads all SQL file name from Directory
	 * 
	 * @param env {@link org.springframework.core.env.Environment}
	 */
	private void readAllSqlFileName(Environment env){
		sqlIdListFromFolder = new ArrayList<String>();
		Path sqlFolderFullPath = Paths.get(env.getProperty("product.repo.folder.path"),Constants.SQL_DIRECTORY_PATH); 
		LOGGER.info("Fetching all checked in SQL files from the directory path: " + sqlFolderFullPath.toString());	
		File sqlFolder = new File(sqlFolderFullPath.toUri());
		File[] sqlFiles = sqlFolder.listFiles();
		for(File sqlFile:sqlFiles) {
			if(sqlFile.isFile()){
				if(sqlFile.getName().indexOf(".")!=-1){
					sqlIdListFromFolder.add(sqlFile.getName().substring(0,sqlFile.getName().indexOf(".")));
				}else{
					sqlIdListFromFolder.add(sqlFile.getName());
				}
				LOGGER.debug("Fetched sql id from product Repo:" + sqlFile.getName());
			}
		}
	}
	
	@Override
	public SqlIssue read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		SqlIssue sqlIssue = null;
		if (index < sqlIdListFromFolder.size()) {
			sqlIssue = new SqlIssue();
			sqlIssue.setSqlId(sqlIdListFromFolder.get(index));
			index++;
		}
		return sqlIssue;
	}

}
