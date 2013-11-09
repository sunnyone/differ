package cz.nkp.differ.compare.metadata.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author xrosecky
 */
public class DifferMetricTransformer implements ResultTransformer {

    private static final String PATTERN_AS_STRING =
	    "(\\w+) : (\\d+\\.\\d+) , (\\d+\\.\\d+) , (\\d+\\.\\d+)";

    private Pattern pattern = Pattern.compile(PATTERN_AS_STRING);

    @Override
    public List<Entry> transform(byte[] stdout, byte[] stderr) throws IOException {
	List<Entry> result = new ArrayList<Entry>();
	BufferedReader bf = new BufferedReader(new StringReader(new String(stdout)));
	String str;
	while ((str = bf.readLine()) != null) {
	    Matcher matcher = pattern.matcher(str);
	    boolean matchFound = matcher.find();
	    if (matchFound && matcher.groupCount() == 4) {
		String metricName = matcher.group(1).trim();
		double redValue = Double.parseDouble(matcher.group(2).trim());
		double greenValue = Double.parseDouble(matcher.group(3).trim());
		double blueValue = Double.parseDouble(matcher.group(4).trim());
		double avg = (redValue + greenValue + blueValue) / 3;
		result.add(createEntry(metricName, "red", redValue));
		result.add(createEntry(metricName, "green", greenValue));
		result.add(createEntry(metricName, "blue", blueValue));
		result.add(createEntry(metricName, "avg", avg));
	    }
	}
	return result;
    }

    private Entry createEntry(String source, String key, Double value) {
	Entry entry = new Entry();
	entry.setSource(source);
	entry.setKey(key);
	entry.setValue(value.toString());
	return entry;
    }

}
