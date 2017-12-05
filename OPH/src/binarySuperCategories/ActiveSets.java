package binarySuperCategories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import features.Feature;

public class ActiveSets {
	public Map<Integer , List<List<Integer> > > activeset;
	public static List<Integer> A;
	public static List<Integer> B;
	
	public ActiveSets(int index , int S , Map<Integer , SuperCategories > C , Map<Integer , HammingSuperCategories > hC , List<Feature> input_features) {
		activeset = new HashMap<Integer , List<List<Integer> > >();
		for(int i = 1 ; i < S-1 ; i++) {
			List<List<Integer> > temp = new ArrayList<List<Integer> >();
			A = new ArrayList<Integer>(calculateAS(C.get(index).getEntry(i) , hC.get(index).getEntry(i-1)));
			B = new ArrayList<Integer>(calculateAS(hC.get(index).getEntry(i+1) , C.get(index).getEntry(i)));
			temp.add(new ArrayList<Integer>(A));
			temp.add(new ArrayList<Integer>(B));
			activeset.put(i, temp);
		}
	}
	
	public static List<Integer> calculateAS(List<List<Integer> > X , List<List<Integer> >  Y) {
		List<Integer> temp = new ArrayList<Integer>();
		for(int i = 0 ; i < X.get(0).size() ; i++) {
			if(!Y.get(0).contains(X.get(0).get(i))) {
				temp.add(X.get(0).get(i));
			}
		}
		return temp;
	}
}
