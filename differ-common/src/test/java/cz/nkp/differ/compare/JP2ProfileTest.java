package cz.nkp.differ.compare;

import cz.nkp.differ.compare.metadata.JP2Kernel;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.JP2Size;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author xrosecky
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jp2ProfileTestsCtx.xml"})
public class JP2ProfileTest {
    
    @Autowired
    Jaxb2Marshaller marshaller;
    
    @Test
    public void serializationTest() throws Exception {
        JP2Profile profile1 = new JP2Profile();
        profile1.setKernel(JP2Kernel.Revesible5x3);
        profile1.setPreccintSizes(Arrays.asList(new JP2Size(64, 64), new JP2Size(128, 128)));
        profile1.setDecompositionLevels(Arrays.asList(4, 5, 6, 7));
        profile1.setProgressionOrders(Arrays.asList("RPCL"));
        profile1.setTileSizes(Arrays.asList(new JP2Size(1024, 1024)));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Result result = new StreamResult(bos);
        marshaller.marshal(profile1, result);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Source source = new StreamSource(bis);
        System.out.println(new String(bos.toByteArray()));
        JP2Profile profile2 = (JP2Profile) marshaller.unmarshal(source);
    }
    
}
