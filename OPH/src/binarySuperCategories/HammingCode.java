package binarySuperCategories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import features.Feature;
import wInitialization.WMatrix;

public class HammingCode {
	public Map<Integer , List<Integer> > hammingCodes;
	
	public HammingCode(List<Feature> input_features , WMatrix w , int m) {
		hammingCodes = new HashMap<Integer , List<Integer> >();
		double wTx = 0.0;
		
		for(int i = 0 ; i < input_features.size() ; i++) {
			List<Integer> code = new ArrayList<Integer>();
			for(int j = 0 ; j < m ; j++) {
				wTx = w.wTransposeX(j, input_features.get(j));
				if (wTx > 0) {
					code.add(1);
				}
				else {
					code.add(0);
				}
			}
			hammingCodes.put(i, code);
		}
	}
	
	public int getHammingScore(int x , int y) {
		int score = 0;
		List<Integer> temp1 = hammingCodes.get(x);
		List<Integer> temp2 = hammingCodes.get(y);
		
		for(int i = 0 ; i < temp1.size() ; i++) {
			if (temp1.get(i) != temp2.get(i)) {
				score++;
			}
		}
		return score;
	}
}
