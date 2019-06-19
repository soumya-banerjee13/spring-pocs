package edu.soumya.spring.sqldiff.plugin.report.util;

import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;

import edu.soumya.spring.sqldiff.plugin.constants.Constants;

/**
 * @author soumyab
 *
 */
public class DiffHtmlReportGenerationUtil {
	private static final String defaultInsertColorCode = "green";
	private static final String defaultDeleteColorCode = "#ef346f";
	
	/**
	 * Convert a Diff list into a pretty HTML report.
	 * <p>
	 * @param diffs LinkedList of Diff objects.
	 * @return HTML representation.
	 */
	public static String diffPrettyHtml(LinkedList<Diff> diffs,String insertColorCode,String deleteColorCode)
	{
		if(StringUtils.isBlank(insertColorCode)) {
			insertColorCode = defaultInsertColorCode;
		}
		if(StringUtils.isBlank(deleteColorCode)) {
			deleteColorCode = defaultDeleteColorCode;
		}
		
		StringBuilder html = new StringBuilder();
		for (Diff aDiff: diffs)
		{
			String text = aDiff.text.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace(Constants.NEW_LINE, "&para;<br>");
			switch (aDiff.operation) {
				case INSERT:
				html.append("<span style=\"background:")
						.append(insertColorCode).append(";\">").append("&lt;")
						.append(Constants.TEXT_MISSING_FILE).append("&gt;").append(text)
						.append("&lt;/").append(Constants.TEXT_MISSING_FILE).append("&gt;")
						.append("</span>");
					break;
				case DELETE:
				html.append("<span style=\"background:")
						.append(deleteColorCode).append(";\">").append("&lt;")
						.append(Constants.TEXT_MISSING_DB).append("&gt;").append(text)
						.append("&lt;/").append(Constants.TEXT_MISSING_DB).append("&gt;")
						.append("</span>");
					break;
				case EQUAL:
					html.append("<span>").append(text).append("</span>");
					break;
			}
		}
		return html.toString();
	}
		
	public static String getHyperlink(String text, String link) {
		String htmlText = text.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace(Constants.NEW_LINE, "<br>");
		StringBuilder hyperLink = new StringBuilder();
		hyperLink.append("<a href=\"").append(link).append("\">").append(htmlText)
				.append("</a>");
		return hyperLink.toString();
	}
	
	public static String getTableHeaderRow(String... headers) {
		StringBuilder headerRow = new StringBuilder();
		headerRow.append("<tr>");
		for (String header : headers) {
			headerRow.append("<th>").append(header).append("</th>");
		}
		headerRow.append("</tr>");
		return headerRow.toString();
	}
	
	public static String getTableDataRow(String... dataList) {
		StringBuilder headerRow = new StringBuilder();
		headerRow.append("<tr>");
		for (String data : dataList) {
			headerRow.append("<td>").append(data).append("</td>");
		}
		headerRow.append("</tr>");
		return headerRow.toString();
	}
	
	public static String getTable(String tableContent) {
		StringBuilder table = new StringBuilder();
		table.append("<table>").append(tableContent).append("</table>");
		return table.toString();
	}
	
	public static String getBoldText(String text) {
		StringBuilder boldText = new StringBuilder();
		boldText.append("<b>").append(text).append("</b>");
		return boldText.toString();
	}
	
	public static String getErrorText(String text) {
		StringBuilder errorText = new StringBuilder();
		errorText.append("<span style=\"color:red\">").append(text)
				.append("</span>");
		return errorText.toString();
	}
	
	public static String getHtmlString(String text) {
		StringBuilder html = new StringBuilder();
		//Adding Table Styles
		html.append("<style>").append("th,td{padding: 5px;text-align: left;}")
				.append("tr:nth-child(even){background-color: #f2f2f2;}")
				.append("tr:hover{background-color:grey;}").append("</style>");
		String htmlText = text.replace("&", "&amp;").replace("&amp;lt;", "&lt;")
				.replace("&amp;gt;", "&gt;").replace(Constants.NEW_LINE, "<br>");
		html.append("<div>").append(htmlText).append("</div>");
		return html.toString();
	}
}