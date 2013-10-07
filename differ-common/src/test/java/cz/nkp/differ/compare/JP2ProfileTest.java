package cz.nkp.differ.compare;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.JP2Kernel;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.JP2Size;
import cz.nkp.differ.compare.metadata.external.KduExpandMetadataExtractor;
import cz.nkp.differ.plugins.tools.CommandRunner;
import cz.nkp.differ.plugins.tools.CommandRunner.CommandOutput;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.easymock.EasyMock.*;

/**
 *
 * @author xrosecky
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jp2ProfileTestsCtx.xml"})
public class JP2ProfileTest {
    
    @Autowired
    private Jaxb2Marshaller marshaller;
    
    @Autowired
    private KduExpandMetadataExtractor kduExpandExtractor;
    
    @Autowired
    private CommandRunner commandRunnerMock;
    
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
    
    @Test
    public void kduExpandMetadataExtractorTest() throws Exception {
        reset(commandRunnerMock);
        byte[] stderr = new byte[]{};
        InputStream is = this.getClass().getResourceAsStream("/output/kdu-expand-example.txt");
        if (is == null) {
            throw new NullPointerException("is");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(is, bos);
        CommandOutput output = new CommandOutput(bos.toByteArray(), stderr);
        output.setExitCode(0);
        expect(commandRunnerMock.runCommandAndWaitForExit(anyObject(File.class), anyObject(List.class))).andReturn(output);
        replay(commandRunnerMock);
        List<ImageMetadata> metadata = kduExpandExtractor.getMetadata(new File("test.jp2"));
        for (ImageMetadata entry: metadata) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        verify(commandRunnerMock);
    }
    
    
    
}
