package edu.soumya.spring.batch.writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.env.Environment;

import edu.soumya.spring.batch.constants.Constants;
import edu.soumya.spring.batch.detail.report.util.DiffHtmlReportGenerationUtil;
import edu.soumya.spring.batch.model.SqlIssue;


/**
 * Writes Diff Report Summary in a file
 * 
 * @author soumyab
 *
 */
public class SqlDiffReportWriter implements ItemWriter<SqlIssue>{
	
	private Environment env;
	
	private StringBuilder diffTableBuilder;
	
	private Integer mismatchCount = 0;
	private Integer totalFileCount = 0;
	
	public SqlDiffReportWriter(Environment env) {
		this.env = env;
		this.diffTableBuilder = new StringBuilder();
	}

	@Override
	public void write(List<? extends SqlIssue> items) throws Exception {
		
		for (SqlIssue item : items) {
			totalFileCount++;
			if (item.getDiffExists().equals(Boolean.TRUE)) {
				if (!(StringUtils.equals(item.getSqlComment(),
						Constants.ISSUE_NOT_FOUND_DB) || StringUtils.equals(
						item.getSqlComment(), Constants.ISSUE_NOT_FOUND_DIR))) {
					diffTableBuilder.append(DiffHtmlReportGenerationUtil
							.getTableDataRow(DiffHtmlReportGenerationUtil
									.getHyperlink(item.getSqlId(),
											getDiffDetailFileLink(item
													.getSqlId())), item
									.getSqlName(), item.getSqlComment()));
				} else {
					diffTableBuilder.append(DiffHtmlReportGenerationUtil
							.getTableDataRow(item.getSqlId(),
									item.getSqlName(), DiffHtmlReportGenerationUtil.getErrorText(item.getSqlComment())));
				}
				mismatchCount++;
			}
		}
	}
	
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) throws IOException{
		StringBuilder finalStringBuilder = new StringBuilder();
		finalStringBuilder
				.append("Total Number of SQL Fetched from DB and CodeBase: ")
				.append(totalFileCount).append(Constants.NEW_LINE)
				.append("Number of SQL(s) Mismatched: ").append(mismatchCount)
				.append(Constants.NEW_LINE).append(Constants.NEW_LINE);
		if (mismatchCount == 0) {
			finalStringBuilder.append(DiffHtmlReportGenerationUtil.getBoldText("No SQL has diff"));
		} else {
			finalStringBuilder
					.append(DiffHtmlReportGenerationUtil
							.getBoldText("Below are the SQL(s) having diff: "))
					.append(Constants.NEW_LINE).append(Constants.NEW_LINE);
			// Insert Table Header
			diffTableBuilder.insert(0,(DiffHtmlReportGenerationUtil.getTableHeaderRow(
				Constants.SQL_ID_HEADER, Constants.SQL_NAME_HEADER,
				Constants.COMMENT_HEADER)));
			finalStringBuilder.append(DiffHtmlReportGenerationUtil.getTable(diffTableBuilder.toString()));
			finalStringBuilder.append(Constants.NEW_LINE).append(Constants.NEW_LINE).append(Constants.DIFF_DETAIL_DESC);
		}
		String htmlString = DiffHtmlReportGenerationUtil.getHtmlString(finalStringBuilder.toString());
		writeDiffReportToFile(htmlString);
		return ExitStatus.COMPLETED;
	}
	
	/**
	 * Writes Summary report of SQL Comparison to the file location supplied by file_location.properties file
	 * 
	 * @param report
	 * @throws IOException
	 */
	private void writeDiffReportToFile(String report) throws IOException {
		Path reportFileFullPath = Paths.get(env.getProperty("sql.diff.dest.path"),env.getProperty("sql.diff.summary.filename"));
		File diffFile = new File(reportFileFullPath.toUri());
		FileUtils.writeStringToFile(diffFile, report);
	}
	
	private String getDiffDetailFileLink(String sqlId) {
		StringBuilder link = new StringBuilder();
		link.append(Constants.DIFF_DETAIL_FOLDER_NAME).append("/")
				.append(sqlId).append(Constants.DIFF_DETAIL_FILE_SUFFIX)
				.append(Constants.HTML_EXTENSION);
		return link.toString();
	}
}
