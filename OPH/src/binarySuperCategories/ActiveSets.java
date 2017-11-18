package binarySuperCategories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import features.Feature;

public class ActiveSets {
	private static List<Feature> activeset;
	private static List<Feature> A;
	private static List<Feature> B;
	
	public ActiveSets(int index , int S , List<Integer> I , Map<Integer , SuperCategories > C , Map<Integer , HammingSuperCategories > hC) {
		for(int i = 0 ; i < S ; i++) {
//			List<List<Feature> > temp_f1 = C.get(index).getEntry(i);
//			List<List<Feature> > temp_f2 = hC.get(index).getEntry(i-1);
//			for(List<Feature> Lf1 : temp_f1) {
//				for(Feature f1 : Lf1) {
//					System.out.println(f1.index + " - Hello");
//					System.out.println(f1.getFeatureVector());
//					break;
//				}
//				break;
//			}
//			System.out.println(temp_f1.size() + " , " + temp_f2.size());
//			this.A = calculateAS(C.get(index).binarySuperCategories.get(i) , hC.get(index).binarySuperCategories.get(i-1));
//			this.B = calculateAS(hC.get(index).binarySuperCategories.get(i+1) , C.get(index).binarySuperCategories.get(i));
		}
	}
	
	public static List<Feature> calculateAS(List<List<Feature> > X , List<List<Feature> >  Y) {
		List<Feature> temp = new ArrayList<Feature>();
//		System.out.println(X.size() + " , " + Y.size());
		for(int i = 0 ; i < X.get(0).size() ; i++) {
			if(!Y.get(0).contains(X.get(0).get(i))) {
				temp.add(X.get(0).get(i));
			}
		}
		return temp;
	}
}
