package cz.nkp.differ.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author Jan Stavěl <stavel.jan@gmail.com>
 */

/**
 * HTMLGlossaryUtil provides html code mapped related to given phrase.
 * Phrase is normalized this way:
 *      s/[\t \ ]+/-/
 *      uppercase all letters
 *      trim left, right
 *
 * Directory structure:
 *    cs_CZ/phrase-1.html
 *          phrase-2.html
 *          phrase-3.html
 *    en_US/phrase-1.html
 *          phrase-2.html
 *          phrase-3.html
 *
 *  Directory is located at resources dir.
 */

public class HTMLGlossaryUtil implements GlossaryUtil {
    public HTMLGlossaryUtil() {
    }
    
    /**
     * @see cz.nkp.differ.tools.GlossaryUtil#getGlossaryFor(java.lang.String, java.util.Locale, java.lang.String)
     */
    @Cacheable("getGlossaryFor")
    @Override
    public String getGlossaryFor(String phrase, Locale locale, String directory) {
        String normalized = this.normalize(phrase);
        String directoryPath = FilenameUtils.normalize("/" + directory + "/" + locale.toString());
        File glossaryPath = new File(directoryPath,normalized + ".html");
        InputStream is = this.getClass().getResourceAsStream(glossaryPath.getPath());
        if( is == null){
            return null;
        }
        Scanner scanner = new Scanner(is,"UTF-8");
        String glossary = scanner.useDelimiter("\\A").next();
        scanner.close();
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return glossary.trim();
    }
    
    /**
     * 
     * @return normalized string
     */
    public String normalize(String val){
        String normalized = java.text.Normalizer.normalize(val, Normalizer.Form.NFD)
            .replaceAll("[^\\p{ASCII}]", "").toLowerCase().trim().replaceAll("[\\ \\t]+","-");
        return normalized;
    }

	/* 
	 * @see cz.nkp.differ.tools.GlossaryUtil#existsGlossaryFor(java.lang.String, java.util.Locale)
	 */
    @Cacheable("existsGlossaryFor")
	@Override
	public Boolean existsGlossaryFor(String phrase, Locale locale, String directory) {
		return getGlossaryFor(phrase, locale, directory) != null;
	}
}
