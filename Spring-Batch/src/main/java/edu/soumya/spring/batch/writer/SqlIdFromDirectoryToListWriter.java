package edu.soumya.spring.batch.writer;

import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.soumya.spring.batch.model.SqlIssue;


/**
 * Writes SQL id from Directory to a list
 * 
 * @author soumyab
 *
 */
public class SqlIdFromDirectoryToListWriter implements ItemWriter<SqlIssue>{
	
	@Qualifier("sqlListFromDirectory")
	@Autowired
	Set<String> sqlListFromDirectory;
	
	@Qualifier("allSqlIdSet")
	@Autowired
	Set<String> allSqlIdSet;
	
	@Override
	public void write(List<? extends SqlIssue> items) throws Exception {
		for(SqlIssue item:items) {
			sqlListFromDirectory.add(item.getSqlId());
			allSqlIdSet.add(item.getSqlId());
		}
	}

}
