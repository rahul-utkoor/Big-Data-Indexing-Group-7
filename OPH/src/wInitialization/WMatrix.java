package wInitialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import features.Feature;

//Intialiazing linear projection W of the hash functions.
public class WMatrix {
	private static int d;
	private static int m;
	private static double b;
	Random rand;
	List<List<Double> > W;
	
	public WMatrix(int d , int m) {
		this.d = d;
		this.m = m;
		this.b = 0.001;
		this.rand = new Random();
		this.W = new ArrayList<List<Double>>();
	}
	
	public void initializeMatrix() {
		for(int i = 0 ; i < this.m ; i++) {
			List<Double> temp = new ArrayList<Double>();
			for(int j = 0 ; j < this.d ; j++) {
				temp.add(randomGen());
			}
			// adding the genearted values to W matrix
			this.W.add(temp);
		}
	}
	// genearting the random values from gaussian distribution
	public double randomGen() {
		return rand.nextGaussian();
	}
	//normaliazing the w matrix
	public void normalize() {
		for(List<Double> sm : W) {
			double norm = Math.sqrt(getNorm(sm));
			for(int i = 0 ; i < sm.size() ; i++) {
				double val = sm.get(i);
				val = val / norm;
				sm.set(i, val);
			}
		}
	}
	//
	public double getNorm(List<Double> sm) {
		double norm = 0.0;
		for(Double val : sm) {
			norm += Math.pow(val.doubleValue(), 2);
		}
		return norm;
	}
	//
	public double wTransposeX(int index , Feature f) {
		double prod = 0.0;
		List<Double> feature_values = f.getFeatureVector();
		for(int i = 0 ; i < this.W.get(index).size() ; i++) {
			prod += (W.get(index).get(i) * feature_values.get(i));
		}
		return (prod + this.b);
	}
}
