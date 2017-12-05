package wInitialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import features.Feature;

public class WMatrix {
	private static int d;
	private static int m;
	private static double b;
	Random rand;
	public List<List<Double> > W;
	
	public WMatrix(int d_d , int m_d) {
		d = d_d;
		m = m_d;
		b = 0.001;
		rand = new Random();
		W = new ArrayList<List<Double>>();
	}
	
	public void initializeMatrix() {
		for(int i = 0 ; i < m ; i++) {
			List<Double> temp = new ArrayList<Double>();
			for(int j = 0 ; j < d ; j++) {
				temp.add(randomGen());
			}
			this.W.add(temp);
		}
	}
	
	public double randomGen() {
		return rand.nextGaussian();
	}
	
	public void normalize() {
		for(List<Double> sm : W) {
			double norm = getNorm(sm);
			for(int i = 0 ; i < sm.size() ; i++) {
				double val = sm.get(i);
				val = val / norm;
				sm.set(i, val);
			}
		}
	}
	
	public double getNorm(List<Double> sm) {
		double norm = 0.0;
		for(Double val : sm) {
			norm += Math.pow(val.doubleValue(), 2);
		}
		return Math.sqrt(norm);
	}
	
	public double wTransposeX(int index , Feature f) {
		double prod = 0.0;
		List<Double> feature_values = f.getFeatureVector();
		for(int i = 0 ; i < this.W.get(index).size() ; i++) {
			prod += (W.get(index).get(i) * feature_values.get(i));
		}
		return (prod + b);
	}
	
	public void update_w(int index , List<Double> removable) {		
		for(int i = 0 ; i < W.get(index).size() ; i++) {
			W.get(index).set(i, 0.001 * (W.get(index).get(i) - removable.get(i)));
		}
	}
}
