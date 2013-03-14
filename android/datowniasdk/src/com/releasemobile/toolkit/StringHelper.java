package com.releasemobile.toolkit;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author Ian
 *
 */
public class StringHelper {


	public static Boolean isNullOrEmpty(String source)
	{
		return source == null || source.length() == 0;
	}
	

	/**
	 * de-accents a string
	 * @param description
	 * @return
	 */
	public static String deaccent(String description) {

	    String nfdNormalizedString = Normalizer.normalize(description, Normalizer.Form.NFD); 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    return pattern.matcher(nfdNormalizedString).replaceAll("");

	}
	
	/**
	 * Ensure's that a sql term will be safe when inserted into a sql string
	 * replaces ' with ''
	 * @param term
	 * @return
	 */
	public static String safeSqlTerm(String term)
	{
		return term.replace("'", "''");
	}
}
