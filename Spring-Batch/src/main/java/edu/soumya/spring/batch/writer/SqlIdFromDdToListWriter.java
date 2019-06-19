package edu.soumya.spring.batch.writer;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.soumya.spring.batch.model.SqlIssue;

/**
 * Writes SQL id from DB to a list
 * 
 * @author soumyab
 *
 */
public class SqlIdFromDdToListWriter implements ItemWriter<SqlIssue>{
	private static Logger LOGGER = LoggerFactory.getLogger(SqlIdFromDdToListWriter.class);
	
	@Qualifier("sqlListFromDB")
	@Autowired
	Set<String> sqlListFromDB;
	
	@Qualifier("allSqlIdSet")
	@Autowired
	Set<String> allSqlIdSet;
	
	@Override
	public void write(List<? extends SqlIssue> items) throws Exception {
		for(SqlIssue item:items) {
			LOGGER.debug("Fetched sql id from DB:" + item.getSqlId());
			sqlListFromDB.add(item.getSqlId());
			allSqlIdSet.add(item.getSqlId());
		}
	}

}
