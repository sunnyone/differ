/**
 * author Jan Stavěl <stavel.jan@gmail.com>
 */
package cz.nkp.differ.tools;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jan Stavěl <stavel.jan@gmail.com>
 *
 */
public class HTMLGlossaryUtilTest {
	private HTMLGlossaryUtil glossary;
	private String testPhrase = "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ";
	private String phraseResult = "This is a funky String";
	private String phraseGlossary = "glossary of a funky String";
			
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.glossary = new HTMLGlossaryUtil(new File("glossary"));
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
		String glossary = this.glossary.getGlossaryFor(this.testPhrase, new Locale("en","US"));
		assertEquals("glossary for test phrase", this.phraseGlossary,glossary);
	}
}
