package cz.nkp.differ.tools;

import java.io.File;
import java.text.Normalizer;
import java.util.Locale;

/**
 * @author Jan Stavěl <stavel.jan@gmail.com>
 */

/**
 * HTMLGlossaryUtil provides html code mapped related to given phrase.
 * Phrase is normalized this way:
 *      s/[\t \ ]+/-/
 *      uppercase all letters
 *
 * Directory structure:
 *    cs_CZ/phrase-1.html
 *          phrase-2.html
 *          phrase-3.html
 *    en_US/phrase-1.html
 *          phrase-2.html
 *          phrase-3.html
 */

public class HTMLGlossaryUtil implements GlossaryUtil {
	private File directory;

	public HTMLGlossaryUtil (File directory){
		
	}
	
	/**
	 * @see cz.nkp.differ.tools.GlossaryUtil#getGlossaryFor(java.lang.String, java.util.Locale)
	 */
	@Override
	public String getGlossaryFor(String phrase, Locale locale) {
		String directory = locale.toString();
		return directory;
	}

	/**
	 * 
	 * @return normalized string
	 */
	public String normalize(String val){
		String normalized = java.text.Normalizer.normalize(val, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
		return normalized;
	}

	/**
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(File directory) {
		this.directory = directory;
	}
}
