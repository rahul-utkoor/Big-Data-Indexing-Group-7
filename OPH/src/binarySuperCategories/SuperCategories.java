package binarySuperCategories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import features.Feature;

public class SuperCategories {
	public static List<List<Integer> > categories;
	public static Map<Integer , List<List<Integer> >> binarySuperCategories;
	
	public SuperCategories(int index , List<Feature> input_features , int m , int N , int S) {
		categories = new ArrayList<List<Integer> >();
		binarySuperCategories = new HashMap<Integer , List<List<Integer> >>();
		
		Feature f = input_features.get(index);
		Map<Integer , Double> sim_scores = new HashMap<Integer , Double>();
		for(int i = 0 ; i < input_features.size() ; i++) {
			double sim = cosineSim(f.getFeatureVector() , input_features.get(i).getFeatureVector());
			sim_scores.put(i , sim);
		}
		sim_scores.remove(index);
		
		Set<Entry<Integer, Double>> set = sim_scores.entrySet();
		List<Entry<Integer, Double>> list = new ArrayList<Entry<Integer, Double>>(set);
		Collections.sort( list, new Comparator<Map.Entry<Integer, Double>>()
        {
            public int compare( Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
		
		List<Integer> temp_vec = new ArrayList<Integer>();
		int counter = 0;
		int temp_var = (int)(N/(m+1));
		for(Map.Entry<Integer, Double> entry:list) {
			if((counter > 0) && (counter % temp_var == 0)) {
				categories.add(temp_vec);
				temp_vec = new ArrayList<Integer>();
			}
			temp_vec.add(entry.getKey());
			counter++;
		}
		categories.add(temp_vec);
		
		// Building Binary super Categories
		for(int i = 0 ; i < S ; i++) {
			List<Integer> temp_arr1 = new ArrayList<Integer>();
			List<Integer> temp_arr2 = new ArrayList<Integer>();
			List<List<Integer> > temp_arr3 = new ArrayList<List<Integer> >();
			for(int j = 0 ; j <= i ; j++) {
				for(Integer f1 : categories.get(j)) {
					temp_arr1.add(f1);
				}
			}
			temp_arr3.add(new ArrayList<Integer>(temp_arr1));
			temp_arr1.clear();
			for(int j = i+1 ; j < m+1 ; j++) {
				for(Integer f1 : categories.get(j)) {
					temp_arr2.add(f1);
				}
			}
			temp_arr3.add(new ArrayList<Integer>(temp_arr2));
			temp_arr2.clear();
			binarySuperCategories.put(i, temp_arr3);
		}
	}
	
	public double cosineSim(List<Double> x1, List<Double> x2) {
		double score = 0.0;
		double norm1 = Math.sqrt(getNorm(x1));
		double norm2 = Math.sqrt(getNorm(x2));
		
		for(int i = 0 ; i < x1.size() ; i++) {
			score += (x1.get(i).doubleValue() * x2.get(i).doubleValue());
		}
		
		return score/(norm1 * norm2);
	}
	
	public double getNorm(List<Double> sm) {
		double norm = 0.0;
		for(Double val : sm) {
			norm += Math.pow(val.doubleValue(), 2);
		}
		return Math.sqrt(norm);
	}
	
	public static List<List<Integer> > getCategories() {
		return categories;
	}
	
	public static Map<Integer , List<List<Integer> > > getBinarySuperCategories() {
		return binarySuperCategories;
	}
	
	public List<List<Integer> > getEntry(int index) {
		return binarySuperCategories.get(index);
	}

}
