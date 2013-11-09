package cz.nkp.differ.compare;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import java.io.File;
import cz.nkp.differ.plugins.tools.CommandRunner.CommandOutput;
import cz.nkp.differ.compare.io.external.ExternalImagesComparator;
import java.util.List;
import cz.nkp.differ.compare.metadata.external.DifferMetricTransformer;
import cz.nkp.differ.compare.metadata.external.ResultTransformer.Entry;
import cz.nkp.differ.plugins.tools.CommandRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.easymock.EasyMock.*;

/**
 *
 * @author xrosecky
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:differMetricImagesComparatorCtx.xml"})
public class DifferMetricImagesComparatorTest {

    private static final String EXAMPLE_OUTPUT = "MSE : 27.2293 , 14.8414 , 19.9627\n"
		+ "PSNR : 33.7804 , 36.4161 , 35.1286";
    
    @Autowired
    private CommandRunner commandRunnerMock;

    @Autowired
    private ExternalImagesComparator externalImagesComparator;
    
    @Test
    public void transformerTest() throws Exception {
	DifferMetricTransformer transformer = new DifferMetricTransformer();
	List<Entry> result = transformer.transform(EXAMPLE_OUTPUT.getBytes(), null);
	Assert.assertTrue(!result.isEmpty());
    }

    @Test
    public void externalImagesComparatorTest() throws Exception {
	reset(commandRunnerMock);
	CommandOutput output = new CommandOutput(EXAMPLE_OUTPUT.getBytes(), null);
        output.setExitCode(0);
        expect(commandRunnerMock.runCommandAndWaitForExit(anyObject(File.class), anyObject(List.class))).andReturn(output);
	replay(commandRunnerMock);
	List<ImageMetadata> metadata = externalImagesComparator.getMetadata(new File("test1.jp2"), new File("test2.jp2"));
	Assert.assertTrue(!metadata.isEmpty());
	verify(commandRunnerMock);
    }


}
