package edu.soumya.spring.batch.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.soumya.spring.batch.model.SqlIssue;


/**
 * Reads SQL Id from both DB and Directory and Send for Diff Report Generation
 * 
 * @author soumyab
 *
 */
public class AllSqlIdReader implements ItemReader<SqlIssue>{
	
	@Qualifier("allSqlIdSet")
	@Autowired
	Set<String> allSqlIdSet;
	
	private Integer index = 0;
	List<String> allSqlIdIndexedList;
	
	public AllSqlIdReader(Set<String> allSqlIdSet){
	}
	
	@BeforeStep
	public void initialize(StepExecution stepExecution){
		allSqlIdIndexedList = new ArrayList<String>();
		for(String sqlIssueId: allSqlIdSet){
			allSqlIdIndexedList.add(sqlIssueId);
		}
	}
	
	@Override
	public SqlIssue read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		SqlIssue sqlIssue = null;
		if (index < allSqlIdIndexedList.size()) {
			sqlIssue = new SqlIssue();
			sqlIssue.setSqlId(allSqlIdIndexedList.get(index));
			index++;
		}
		return sqlIssue;
	}

}