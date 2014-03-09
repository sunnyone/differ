/**
 * author Jan Stavěl <stavel.jan@gmail.com>
 */
package cz.nkp.differ.tools;

import static org.junit.Assert.*;

import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jan Stavěl <stavel.jan@gmail.com>
 *
 */

public class HTMLGlossaryUtilTest {
	private HTMLGlossaryUtil glossary;
	private String testPhrase = "Tĥïŝ ĩš â fůňķŷ  Šťŕĭńġ";
	private String phraseResult = "this-is-a-funky-string";
	private String phraseGlossary = "glossary of a funky string cs_CZ";
	private String versionOfExtractorPhrase = "Version of  Extractor ";
	private String versionOfExtractorNormalizedPhrase="version-of-extractor";
	private String versionOfExtractorGlossaryCS = "Verze extraktoru, co byla použita. Například: <em>Daitss 3.0</em>";
	private String versionOfExtractorGlossaryEN = "Version of extractor used when transforming an image.";
			
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.glossary = new HTMLGlossaryUtil();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOfNormalize() {
		String normalizedString = this.glossary.normalize(this.testPhrase);
		assertEquals("normalize string",this.phraseResult,normalizedString);
	}
	
	@Test
	public void testOfGlossary(){
		String glossary = this.glossary.getGlossaryFor(this.testPhrase, new Locale("cs","CZ"));
		assertEquals("glossary for test phrase", this.phraseGlossary,glossary);
	}
	
	@Test
	public void testOfExistenceGlossary(){
		Boolean existence = this.glossary.existsGlossaryFor(this.testPhrase, new Locale("cs","CZ"));
		assertEquals("existence of a glossary for test phrase", existence, true);
	}

	@Test
	public void testOfNonExistenceGlossary(){
		Boolean existence = this.glossary.existsGlossaryFor("unknown phrase", new Locale("cs","CZ"));
		assertEquals("existence of a glossary for test phrase", existence, false);
	}

	@Test
	public void testOfVersionOfExtractor(){
		String normalizedString = this.glossary.normalize(this.versionOfExtractorPhrase);
		assertEquals("normalize string", this.versionOfExtractorNormalizedPhrase, normalizedString);
		String glossary = this.glossary.getGlossaryFor(this.versionOfExtractorPhrase, new Locale("en","US"));
		assertEquals("glossary for en_US, Version of Extractor",this.versionOfExtractorGlossaryEN,glossary);
		glossary = this.glossary.getGlossaryFor(this.versionOfExtractorPhrase, new Locale("cs","CZ"));
		assertEquals("glossary for cs_CZ, Version of Extractor",this.versionOfExtractorGlossaryCS,glossary);
	}
}
