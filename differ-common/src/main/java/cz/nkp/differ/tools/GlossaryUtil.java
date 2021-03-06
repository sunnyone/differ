/**
 * 
 */
package cz.nkp.differ.tools;

import java.util.Locale;

/**
 * @author Jan Stavěl <stavel.jan@gmail.com>
 *
 */
public interface GlossaryUtil {
	/*
	 * provide String glossary for given phrase and given environment locale.
	 */
	public String getGlossaryFor (String phrase, Locale locale, String directory);
	
	/*
	 * whether the glossary exists for given phrase.
	 */
	public Boolean existsGlossaryFor (String phrase, Locale locale, String directory);
}
