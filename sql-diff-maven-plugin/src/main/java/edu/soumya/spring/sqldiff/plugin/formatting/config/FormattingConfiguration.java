package edu.soumya.spring.sqldiff.plugin.formatting.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import edu.soumya.spring.sqldiff.plugin.constants.Constants;

/**
 * Configuration class used for string formatting
 * 
 * @author soumyab
 *
 */
public class FormattingConfiguration {
	public static final String REPLACE_FORMAT_SEPERATOR = "->";
	private static Logger LOGGER = LoggerFactory.getLogger(FormattingConfiguration.class);
	
	private Environment environment;
	
	public FormattingConfiguration(Environment environment) {
		this.environment = environment;
	}
	
	/**
	 * Formats a string based on the properties in sql-format-config.properties file
	 * 
	 * @param str String to format
	 * @return
	 */
	public String format(String str) {
		boolean trimNeeded = Boolean.parseBoolean(environment.getProperty("sql.format.trim.needed").trim());
		String removePatterns = environment.getProperty("sql.format.remove.patterns");
		String replacePatterns = environment.getProperty("sql.format.replace.patterns");
		
		String processedString = str;
		processedString = trim(processedString,trimNeeded);
		processedString = removeSpecifiedPatterns(processedString,removePatterns);
		processedString = replaceSpecifiedPatterns(processedString,replacePatterns);
		
		return processedString;
	}
	
	/**
	 * Trims a string based on the flag
	 * 
	 * @param str String to process
	 * @param trimRequired trim required
	 * @return Processed string
	 */
	private String trim(String str,boolean trimRequired) {
		return (trimRequired)?str.trim():str;
	}
	
	/**
	 * Removes specified patterns from the string supplied
	 * 
	 * @param str String to process
	 * @param patterns Patterns or Regular Expressions supplied as comma separated string
	 * @return Processed String
	 */
	private String removeSpecifiedPatterns(String str, String patterns) {
		String[] patternsArray = patterns.split(Constants.COMMA_SEPERATOR);
		if(StringUtils.isBlank(patterns) || patternsArray.length==0) return str;
		String processedString = str;
		for(String pattern:patternsArray) {
			if(StringUtils.isBlank(pattern)) {
				LOGGER.warn("One of the remove pattern specified in properties file(sql-format-config.properties) is not proper. Not replacing any pattern");
				return str;
			}
			processedString = processedString.replaceAll(pattern.trim(), StringUtils.EMPTY);
		}
		return processedString;
	}
	
	/**
	 * Replaces specified patterns by the specified string from the string supplied
	 * 
	 * @param str String to process
	 * @param patterns Patterns or Regular Expressions -> String supplied as comma separated string
	 * @return Processed String
	 */
	private String replaceSpecifiedPatterns(String str, String patterns) {
		patterns = patterns.trim();
		String[] patternsArray = patterns.split(Constants.COMMA_SEPERATOR);
		if(StringUtils.isBlank(patterns) || patternsArray.length==0) return str;
		String processedString = str;
		for(String pattern:patternsArray) {
			String[] fromToPattern = pattern.split(REPLACE_FORMAT_SEPERATOR);
			if(StringUtils.isBlank(pattern) || !(pattern.contains(REPLACE_FORMAT_SEPERATOR) && fromToPattern.length ==2)) {
				LOGGER.warn("One of the replace pattern specified in properties file(sql-format-config.properties) is not proper. Not replacing any pattern");
				return str;
			}
			fromToPattern[0] = fromToPattern[0].trim().replaceAll(Constants.COMMA_AS_PATTERN, Constants.COMMA_SEPERATOR);
			fromToPattern[1] = fromToPattern[1].trim().replaceAll(Constants.COMMA_AS_PATTERN, Constants.COMMA_SEPERATOR).replaceAll(Constants.BLANK_SPACE_AS_PATTERN, StringUtils.SPACE);
			processedString = processedString.replaceAll(fromToPattern[0], fromToPattern[1]);
		}
		return processedString;
	}
}
