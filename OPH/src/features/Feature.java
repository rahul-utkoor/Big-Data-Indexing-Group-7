package features;

import java.util.ArrayList;
import java.util.List;

public class Feature {
	List<Double> values;
	public int index;
	public Feature(String str , int index) {
		this.index = index;
		this.values = new ArrayList<>();
		setFeatureVector(str);
	}
	public void setFeatureVector(String line) {
		for(String val : line.split(" ")) {
			values.add(Double.parseDouble(val));
		}
	}
	public List<Double> getFeatureVector() {
		return values;
	}
}
