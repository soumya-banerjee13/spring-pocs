package edu.soumya.spring.sqldiff.plugin.report.creator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import edu.soumya.spring.sqldiff.plugin.constants.Constants;
import edu.soumya.spring.sqldiff.plugin.model.SQLIssue;
import edu.soumya.spring.sqldiff.plugin.report.util.DiffHtmlReportGenerationUtil;

/**
 * @author soumyab
 *
 */
public class DiffSummaryReportCreator {
	private Environment env;

	public DiffSummaryReportCreator(Environment env) {
		this.env = env;
	}

	public void createReport(List<SQLIssue> items) throws IOException {
		int totalFileCount = items.size();
		int mismatchCount = 0;
		StringBuilder diffTableBuilder = new StringBuilder();
		for (SQLIssue item : items) {
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
		writeDiffReportToFile(generateHtmlReportString(totalFileCount,mismatchCount,diffTableBuilder));
	}
	
	private String generateHtmlReportString(int totalFile,int totalMismatch,StringBuilder diffTableBuilder) {
		StringBuilder finalStringBuilder = new StringBuilder();
		finalStringBuilder
				.append("Total Number of SQL Fetched from DB and CodeBase: ")
				.append(totalFile).append(Constants.NEW_LINE)
				.append("Number of SQL(s) Mismatched: ").append(totalMismatch)
				.append(Constants.NEW_LINE).append(Constants.NEW_LINE);
		if (totalMismatch == 0) {
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
		return DiffHtmlReportGenerationUtil.getHtmlString(finalStringBuilder.toString());
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
