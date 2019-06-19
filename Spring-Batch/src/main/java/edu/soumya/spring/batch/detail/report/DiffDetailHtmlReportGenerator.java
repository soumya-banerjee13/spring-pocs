package edu.soumya.spring.batch.detail.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;
import org.springframework.core.env.Environment;

import edu.soumya.spring.batch.constants.Constants;
import edu.soumya.spring.batch.detail.report.util.DiffHtmlReportGenerationUtil;


/**
 * @author soumyab
 *
 * <br>
 * This Class Uses Google Diff Match Patch
 * Library to generate diff report as html format.
 * 
 */
public class DiffDetailHtmlReportGenerator {
	
	private Environment env;
	
	private DiffMatchPatch diffMatchPatch;
	
	public DiffDetailHtmlReportGenerator(Environment env) {
		this.env = env;
		this.diffMatchPatch = new DiffMatchPatch();
	}
	
	/**
	 * Generates Diff Report Using SQL Id
	 * 
	 * @param contentOld sql for SQL fetched from directory as old content
	 * @param contentNew sql for SQL fetched from DB as new content
	 * @param sqlId SQL Id
	 * @throws IOException
	 */
	public void generateDiffReport(String contentOld, String contentNew, String sqlId) throws IOException {
		LinkedList<Diff> diffResult = diffMatchPatch.diffMain(contentOld,contentNew);
		diffMatchPatch.diffCleanupSemantic(diffResult);
		String diffHtml = DiffHtmlReportGenerationUtil.diffPrettyHtml(diffResult,getInsertColorCode(),getDeleteColorCode());
		String reportId = sqlId + Constants.DIFF_DETAIL_FILE_SUFFIX;
		writeDiffDetailReportToFile(reportId,diffHtml);
	}
	
	private String getInsertColorCode() {
		return env.getProperty("sql.fomat.diff.missinginfile.color.code").trim();
	}
	
	private String getDeleteColorCode() {
		return env.getProperty("sql.fomat.diff.missingindb.color.code").trim();
	}
	
	/**
	 * Writes diff report in the location mentioned in file-location.properties file
	 * 
	 * @param reportId name of the HTML report
	 * @param reportContent content of the html report
	 * @throws IOException
	 */
	private void writeDiffDetailReportToFile(String reportId, String reportContent) throws IOException {
		
		String filenameWithExt = reportId + Constants.HTML_EXTENSION;
		Path reportFileFullPath = Paths.get(env.getProperty("sql.diff.dest.path"), Constants.DIFF_DETAIL_FOLDER_NAME, filenameWithExt);
		File diffDetailFile = new File(reportFileFullPath.toUri());
		FileUtils.writeStringToFile(diffDetailFile, reportContent);
	}
}
