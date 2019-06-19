package edu.soumya.spring.batch.util;

import org.apache.commons.lang3.SystemUtils;

import edu.soumya.spring.batch.constants.Constants;
import edu.soumya.spring.batch.formatting.config.FormattingConfiguration;


/**
 * @author soumyab
 *
 */
public class StringFormatterUtils {
	
	/**
	 * Line Separator for Windows
	 */
	public static final String LINE_SEPERATOR_WINDOWS = "\r\n";
	
	/**
	 * Line Separator for Linux
	 */
	public static final String LINE_SEPERATOR_LINUX = "\n";
	
	/**
	 * Default Line Separator
	 */
	public static final String LINE_SEPERATOR_DEFAULT = "\n";
	
	/**
	 * Eliminates OS dependency for the Strings having new line
	 * 
	 * @param str String to Process
	 * @return Processed String
	 */
	public static String replaceNewLineOSDependency(String str) {
		String osSpecificLineRegex = (SystemUtils.IS_OS_LINUX)?LINE_SEPERATOR_LINUX:(SystemUtils.IS_OS_WINDOWS)?LINE_SEPERATOR_WINDOWS:LINE_SEPERATOR_DEFAULT;
		return str.replaceAll(osSpecificLineRegex, Constants.NEW_LINE);
	}
	
	/**
	 * Processes a string based on the Configuration
	 * 
	 * @param str String to Process
	 * @param formatConfig {@link FormattingConfiguration}
	 * @return Processed String
	 */
	public static String configurationSpecificModification(String str, FormattingConfiguration formatConfig) {
		return formatConfig.format(str);
	}
}
