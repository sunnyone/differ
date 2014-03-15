package cz.nkp.differ.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.core.io.Resource;

public class ExampleProviderImpl implements ExampleProvider {
	
	private List<Example> examples = Collections.emptyList();
	
	@Override
	public List<Example> getExamples() {
		return examples;
	}

	public void setPropertiesFileWithExamples(Resource resource) throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(resource.getInputStream());
		examples = new ArrayList<Example>(props.size());
		for (Entry<Object, Object> entry : props.entrySet()) {
			String name = (String) entry.getKey();
			String files = (String) entry.getValue();
			String filesAsArray[] = files.split(",");
			Example example = new Example();
			example.setFile1(new File(filesAsArray[0]));
			example.setFile2(new File(filesAsArray[1]));
			example.setName(name);
			examples.add(example);
		}
	}

}
