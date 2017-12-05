package mainPack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import binarySuperCategories.ActiveSets;
import binarySuperCategories.HammingCode;
import binarySuperCategories.HammingSuperCategories;
import binarySuperCategories.SuperCategories;
import features.Feature;
import wInitialization.WMatrix;

public class OPH {
	public static int d;
	public static int m;
	public static int N;
	public static double mu;
	public static int lambda = 10;
	public static int S = 10;
	public static double k = 1.5; 
	public static String inputPath = "";
	public static int max_outer_iter = 10;
	public static int max_inner_iter = 10;
	public static int mini_batch_samples = 200;
	public static double alpha;
	public static List<Integer> I_;
	public static Map<Integer , SuperCategories > input_Categories;
	public static Map<Integer , HammingSuperCategories > hamming_Categories;
	public static List<Feature> input_features;
	public static WMatrix w;
	
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Too few arguments\n" + "Please enter m value and Input file path");
			return;
		}
		m = Integer.parseInt(args[0]);
		inputPath = args[1];
		System.out.println("Reading Input file");
		readFile(inputPath);
		w = new WMatrix(d , m);
		System.out.println("Initializing W matrix");
		w.initializeMatrix();
		System.out.println("Normalizing W matrix");
		w.normalize();
		System.out.println("Calculate Alpha");
		alpha = (2 * Math.log(1000000))/maxZ();
		System.out.println("Dividing Input data into binary super categories");
		input_Categories = new HashMap<Integer , SuperCategories >();
		for(int i = 0 ; i < N ; i++) {
			SuperCategories sc = new SuperCategories(i , input_features , m , N , S);
			input_Categories.put(i, sc);
		}
		run_oph();
		writeFile();
		System.out.println("Program Execution Completed");
		
	}
	
	public static void writeFile() {
		File file = new File("./dataset/output.txt");
		FileWriter writer = null;
		try {
	        writer = new FileWriter(file);
	        for(int i = 0 ; i < w.W.size() ; i++) {
	        	for(int j = 0 ; j < w.W.get(i).size() ; j++) {
	        		writer.write(w.W.get(i).get(j) + " ");
	        	}
	        	writer.write("\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace(); // I'd rather declare method with throws IOException and omit this catch.
	    } finally {
	        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
	    }
	}
	
	public static void readFile(String filePath) {
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF8");
			BufferedReader br = new BufferedReader(reader);
			String line;
			int counter = 0;
			
			input_features = new ArrayList<Feature>();
			Feature img_data = null;
			while((line = br.readLine()) != null) {
				img_data = new Feature(line , counter);
				input_features.add(img_data);
				counter++;
			}
			br.close();
			N = counter;
			d = img_data.getFeatureVector().size();
		}
		catch(Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}
	
	public static double maxZ() {
		double al = 0.0;
		double max_val = -10000;
		for(int i = 0 ; i < N ; i++) {
			for(int j = 0 ; j < m ; j++) {
				al = w.wTransposeX(j , input_features.get(i));
				if(al > max_val) {
					max_val = al;
				}
			}
		}
		return max_val;
	}
	
	public static void run_oph() {
		System.out.println("Finding error and updating hash functions");
		for(int i = 0 ; i < m ; i++) {
			mu = 1;
			for(int outer_iter = 0 ; outer_iter < max_outer_iter ; outer_iter++) {
				for(int inner_iter = 0 ; inner_iter < max_inner_iter ; inner_iter++) {
					List<Double> error = null;
					HammingCode hamming_codes = new HammingCode(input_features , w , m);
//					System.out.println("Generating Random samples for stocastic gradient descent");
					I_ = new ArrayList<Integer>();
					I_ = generate_random_samples();
					hamming_Categories = new HashMap<Integer , HammingSuperCategories >();
					
//					System.out.println("Building binary super categories in hamming space");
					for(Integer x : I_) {
						HammingSuperCategories hsc = new HammingSuperCategories(x , hamming_codes , S);
						hamming_Categories.put(x, hsc);
					}
					
					Map<Integer , ActiveSets> active_sets = new HashMap<Integer , ActiveSets>();
//					System.out.println("Building Active sets");
					for(Integer x : I_) {
						ActiveSets as = new ActiveSets(x , S , input_Categories , hamming_Categories , input_features);
						active_sets.put(x, as);
					}
					
					for(Integer x : I_) {
						List<Double> x_val = new ArrayList<Double>(input_features.get(x).getFeatureVector());
						for(int hd = 1 ; hd < S-1 ; hd++) {
							double gm1 = gamma(m-hd);
							double gm2 = gamma(hd+1);
							List<Double> A_val = null;
							List<Double> B_val = null;
							
							for(Integer x1 : active_sets.get(x).activeset.get(hd).get(0)) {
								List<Double> x1_val = new ArrayList<Double>(input_features.get(x1).getFeatureVector());
								double beta = calculate_beta(input_features.get(x) , input_features.get(x1));
								int hscor = hamming_codes.getHammingScore(x, x1);
								double wtx = w.wTransposeX(i, input_features.get(x));
								double wtx1 = w.wTransposeX(i, input_features.get(x1));
								double temp1 = sigmoid(wtx , beta);
								double temp2 = sigmoid(wtx1 , beta);
								double temp3 = gm1 * sigmoid(hscor-hd , gm1) * (1 - sigmoid(hscor-hd , gm1));
								double temp4 = 2 * (temp1 - temp2) * temp3;
								double temp5 = beta * temp1 * (1 - temp1);
								double temp6 = beta * temp2 * (1 - temp2);
								
								if(A_val == null) {
									A_val = new ArrayList<Double>();
									for(int abc = 0 ; abc < x_val.size() ; abc++) {
										double temp7 = (temp4 * temp5 * x_val.get(abc)) - (temp4 * temp6 * x1_val.get(abc));
										A_val.add(temp7);
									}
								}
								else {
									for(int abc = 0 ; abc < x_val.size() ; abc++) {
										double temp7 = (temp4 * temp5 * x_val.get(abc)) - (temp4 * temp6 * x1_val.get(abc));
										A_val.set(abc, A_val.get(abc)+temp7);
									}
								}
							}
							
							for(Integer x1 : active_sets.get(x).activeset.get(hd).get(1)) {
								List<Double> x1_val = new ArrayList<Double>(input_features.get(x1).getFeatureVector());
								double beta = calculate_beta(input_features.get(x) , input_features.get(x1));
								int hscor = hamming_codes.getHammingScore(x, x1);
								double wtx = w.wTransposeX(i, input_features.get(x));
								double wtx1 = w.wTransposeX(i, input_features.get(x1));
								double temp1 = sigmoid(wtx , beta);
								double temp2 = sigmoid(wtx1 , beta);
								double temp3 = gm2 * sigmoid(hscor-hd , gm2) * (1 - sigmoid(hscor-hd , gm2));
								double temp4 = 2 * (temp1 - temp2) * temp3;
								double temp5 = beta * temp1 * (1 - temp1);
								double temp6 = beta * temp2 * (1 - temp2);
								
								if(B_val == null) {
									B_val = new ArrayList<Double>();
									for(int abc = 0 ; abc < x_val.size() ; abc++) {
										double temp7 = (temp4 * temp5 * x_val.get(abc)) - (temp4 * temp6 * x1_val.get(abc));
										B_val.add(temp7);
									}
								}
								else {
									for(int abc = 0 ; abc < x_val.size() ; abc++) {
										double temp7 = (temp4 * temp5 * x_val.get(abc)) - (temp4 * temp6 * x1_val.get(abc));
										B_val.set(abc, B_val.get(abc)+temp7);
									}
								}
								
							}
//							System.out.print("A size : " + active_sets.get(x).activeset.get(hd).get(0).size());
//							System.out.println(", B size : " + active_sets.get(x).activeset.get(hd).get(1).size());
							
							if(A_val != null && B_val != null) {
								if(error == null) {
									error = new ArrayList<Double>();
									for(int abc = 0 ; abc < A_val.size() ; abc++) {
										error.add(A_val.get(abc) + (lambda * B_val.get(abc)));
									}
								}
								else {
									for(int abc = 0 ; abc < A_val.size() ; abc++) {
										error.set(abc, error.get(abc)+(A_val.get(abc) + (lambda * B_val.get(abc))));
									}
								}
							}
							else if(A_val != null) {
								if(error == null) {
									error = new ArrayList<Double>();
									for(int abc = 0 ; abc < A_val.size() ; abc++) {
										error.add(A_val.get(abc));
									}
								}
								else {
									for(int abc = 0 ; abc < A_val.size() ; abc++) {
										error.set(abc, error.get(abc)+A_val.get(abc));
									}
								}
							}
							else if(B_val != null) {
								if(error == null) {
									error = new ArrayList<Double>();
									for(int abc = 0 ; abc < B_val.size() ; abc++) {
										error.add((lambda * B_val.get(abc)));
									}
								}
								else {
									for(int abc = 0 ; abc < B_val.size() ; abc++) {
										error.set(abc, error.get(abc)+(lambda * B_val.get(abc)));
									}
								}
							}
						}
					}
					
					System.out.println("Updating w" + i + " for outer_loop : " + outer_iter + " for inner_loop : " + inner_iter);
					w.update_w(i, error);
					writeFile();
				}
				mu = k * mu;
			}
			System.out.println("W : " + i + " completed");
		}
	}
	
	public static List<Integer> generate_random_samples() {
		List<Integer> temp = new ArrayList<Integer>();
		Random rand = new Random();
		int temp_var = 0;
		for(int i = 0 ; i < mini_batch_samples ; i++) {
			temp_var = rand.nextInt() % N;
			if (temp_var < 0) {
				if(!temp.contains(N + temp_var)) {
					temp.add(N + temp_var);					
				}
				else {
					i--;
				}
			}
			else {
				if(!temp.contains(temp_var)) {
					temp.add(temp_var);					
				}
				else {
					i--;
				}
			}
		}
		return temp;
	}
	
	public static double gamma(int hd) {
		return ((2 * Math.log(1000000))/(hd));
	}
	
	public static double calculate_beta(Feature f1 , Feature f2) {
		double v1 = 0 , v2 = 0;
		List<Double> vec1 = new ArrayList<Double>(f1.getFeatureVector());
		List<Double> vec2 = new ArrayList<Double>(f2.getFeatureVector());
		
		for(int i = 0 ; i < vec1.size() ; i++) {
			v1 += Math.pow(vec1.get(i), 2);
			v2 += Math.pow(vec2.get(i), 2);
		}
		v1 = Math.sqrt(v1);
		v2 = Math.sqrt(v2);
		
		return (2 * Math.log(1000000))/Math.sqrt(Math.max(v1, v2) +1);
	}
	
	private static double sigmoid(double z , double alpha) {
		double sigVal = (1 / (1 + Math.exp(-(alpha * z))));
		return sigVal;
	}

}
