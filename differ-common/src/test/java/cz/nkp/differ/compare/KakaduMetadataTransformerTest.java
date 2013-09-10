package cz.nkp.differ.compare;

import cz.nkp.differ.compare.metadata.external.KakaduMetadataTransformer;
import cz.nkp.differ.compare.metadata.external.ResultTransformer;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.annotation.Resource;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author xrosecky
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:kduexpandTestsCtx.xml", "classpath:jp2ProfileTestsCtx.xml"})
public class KakaduMetadataTransformerTest {
    
    @Resource(name="kakaduMetadataTransformer")
    ResultTransformer kakaduTransformer;
        
    @Autowired
    Jaxb2Marshaller marshaller;
    
    @Test
    public void serializationTest() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/output/kdu_expand_example.txt");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(is, bos);
        kakaduTransformer.transform(bos.toByteArray(), null);
    }
}
