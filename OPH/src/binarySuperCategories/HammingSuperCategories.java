package binarySuperCategories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import features.Feature;
import wInitialization.WMatrix;

public class HammingSuperCategories {
//	public static List<List<Feature> > categories;
//	public static Map<Integer , List<List<Feature> >> binarySuperCategories;
//	public static List<Integer> hammingCode;
	public static Map<Integer , List<List<Integer> >> binarySuperCategories;
	
	public HammingSuperCategories(int index , HammingCode hamming_codes , int S) {
		binarySuperCategories = new HashMap<Integer , List<List<Integer> >>();
		
		int score;
		List<Integer> temp1 = new ArrayList<Integer>();
		Map<Integer , List<Integer> > temp_map = new HashMap<Integer , List<Integer> >();
		
		for(int i = 0 ; i < hamming_codes.hammingCodes.size() ; i++) {
			temp1.add(i);
		}
		for(int i = 0 ; i < hamming_codes.hammingCodes.size() ; i++) {
			score = hamming_codes.getHammingScore(i, index);
			if(i != index) {
				if(temp_map.containsKey(score)) {
					temp_map.get(score).add(i);					
				}
				else {
					List<Integer> temp2 = new ArrayList<Integer>();
					temp2.add(i);
					temp_map.put(score, temp2);
				}
			}
		}
		
		for(int i = 0 ; i < S ; i++) {
			List<Integer> temp3;
			List<List<Integer> > temp4 = new ArrayList<List<Integer> >();
			if(temp_map.containsKey(i)) {
				temp3 = new ArrayList<Integer>(temp1);
				temp4.add(temp_map.get(i));
				temp3.removeAll(temp_map.get(i));
				temp4.add(temp3);
			}
			else {
				temp3 = new ArrayList<Integer>();
				temp4.add(temp3);
				temp4.add(temp1);
			}
			binarySuperCategories.put(i, temp4);
		}
	}
	
//	public void HammingSuperCategories1(int index , List<Feature> input_features , WMatrix w , int N , int S , int m , double alpha) {
//		categories = new ArrayList<List<Feature> >();
//		binarySuperCategories = new HashMap<Integer , List<List<Feature> >>();
//		
//		Feature f = input_features.get(index);
//		Map<Integer , Double> sim_scores = new HashMap<Integer , Double>();
//		hammingCode = getHammingCode(f , w , m , alpha);
//		
//		for(int i = 0 ; i < input_features.size() ; i++) {
//			double sim = hammingSim(hammingCode , input_features.get(i) , w , m , alpha);
//			sim_scores.put(i , sim);
//		}
//		sim_scores.remove(index);
//		
//		Set<Entry<Integer, Double>> set = sim_scores.entrySet();
//		List<Entry<Integer, Double>> list = new ArrayList<Entry<Integer, Double>>(set);
//		Collections.sort( list, new Comparator<Map.Entry<Integer, Double>>()
//        {
//            public int compare( Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2 )
//            {
//                return (o2.getValue()).compareTo( o1.getValue() );
//            }
//        } );
//		
//		List<Feature> temp_vec = new ArrayList<Feature>();
//		int counter = 0;
//		int temp_var = (int)(N/(m+1));
//		for(Map.Entry<Integer, Double> entry:list) {
//			if((counter > 0) && (counter % temp_var == 0)) {
//				categories.add(temp_vec);
//				temp_vec = new ArrayList<Feature>();
//			}
//			temp_vec.add(input_features.get(entry.getKey()));
//			counter++;
//		}
//		categories.add(temp_vec);
//		
//		// Building Binary super Categories
//		for(int i = 0 ; i < S ; i++) {
//			List<Feature> temp_arr1 = new ArrayList<Feature>();
//			List<Feature> temp_arr2 = new ArrayList<Feature>();
//			List<List<Feature> > temp_arr3 = new ArrayList<List<Feature> >();
//			for(int j = 0 ; j <= i ; j++) {
//				for(Feature f1 : categories.get(j)) {
//					temp_arr1.add(f1);
//				}
//			}
//			temp_arr3.add(new ArrayList<Feature>(temp_arr1));
//			temp_arr1.clear();
//			for(int j = i+1 ; j < m+1 ; j++) {
//				for(Feature f1 : categories.get(j)) {
//					temp_arr2.add(f1);
//				}
//			}
//			temp_arr3.add(new ArrayList<Feature>(temp_arr2));
//			temp_arr2.clear();
//			binarySuperCategories.put(i, temp_arr3);
//		}
//	}
//	
//	public static double hammingSim(List<Integer> hammingCode1, Feature x2 , WMatrix w , int m , double alpha) {
//		double score = 0;
//		
//		List<Integer> hammingCode2 = getHammingCode(x2 , w , m , alpha);
//		score = getHammingScore(hammingCode1 , hammingCode2);
//
//		return score;
//	}
//	
//	public static List<Integer> getHammingCode(Feature f , WMatrix w , int m , double alpha) {
//		List<Integer> code = new ArrayList<Integer>();
//		
//		double wTx = 0.0;
//		for(int i = 0 ; i < m ; i++) {
//			wTx = w.wTransposeX(i, f);
////			code.add(sigmoid(wTx , alpha));
//			code.add(signum(wTx));
//		}
//		
//		return code;
//	}
//	
//	public static int getHammingScore(List<Integer> x1 , List<Integer> x2) {
//		int score = 0;
////		double norm1 = Math.sqrt(getNorm(x1));
////		double norm2 = Math.sqrt(getNorm(x2));
////		
////		for(int i = 0 ; i < x1.size() ; i++) {
////			score += (x1.get(i).doubleValue() * x2.get(i).doubleValue());
////		}
////		
////		return score/(norm1 * norm2);
//		for(int i = 0 ; i < x1.size() ; i++) {
//			if (x1.get(i) != x2.get(i)) {
//				score++;
//			}
//		}
//		return score;
//	}
//	
//	private static int signum(double z) {
//		if (z > 0) {
//			return 1;
//		}
//		return 0;
//	}
//	
////	private static double sigmoid(double z , double alpha) {
////		double sigVal = (1 / (1 + Math.exp(-(alpha * z))));
////		return sigVal;
////	}
////	
////	private static double getNorm(List<Double> sm) {
////		double norm = 0.0;
////		for(Double val : sm) {
////			norm += Math.pow(val.doubleValue(), 2);
////		}
////		return norm;
////	}
//	
//	public static List<List<Feature> > getCategories() {
//		return categories;
//	}
//	
//	public static Map<Integer , List<List<Feature> > > getBinarySuperCategories() {
//		return binarySuperCategories;
//	}
	
	public List<List<Integer> > getEntry(int index) {
		return binarySuperCategories.get(index);
	}
	
}
