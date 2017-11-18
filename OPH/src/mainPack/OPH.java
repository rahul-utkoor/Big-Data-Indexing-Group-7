package mainPack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import binarySuperCategories.ActiveSets;
import binarySuperCategories.HammingSuperCategories;
import binarySuperCategories.SuperCategories;
import features.Feature;
import wInitialization.WMatrix;

public class OPH {
	private static int d;
	private static int m;
	private static int N;
	private static int S = 10;
	private static double mu = 1;
	private static double k = 1.5; 
	private static String inputPath = "";
	private static int max_outer_iter = 10;
	private static int max_inner_iter = 10;
	private static int mini_batch_samples = 200;
	private static double alpha;
	private static List<Integer> I_;
	private static Map<Integer , SuperCategories > Categories;
	private static Map<Integer , HammingSuperCategories > hamming_Categories;
	private static List<Feature> input_features;
	private static WMatrix w;
	
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
		System.out.println("Dividing Input data into categories");
		Categories = new HashMap<Integer , SuperCategories >();
		for(int i = 0 ; i < N ; i++) {
			SuperCategories sc = new SuperCategories(i , input_features , m , N , S);
			Categories.put(i, sc);
		}
		run_oph();
		System.out.println("Program Execution Completed");
		
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
//			System.out.println("Total data : " + N);
//			System.out.println("Total dimensions : " + img_data.getFeatureVector().size());
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
		for(int i = 0 ; i < max_outer_iter ; i++) {
			for(int j = 0 ; j < max_inner_iter ; j++) {
				hamming_Categories = new HashMap<Integer , HammingSuperCategories >();
				I_ = new ArrayList<Integer>();
				I_ = generate_random_samples();
				System.out.println(I_);
				for(Integer x : I_) {
					HammingSuperCategories hsc = new HammingSuperCategories(x , input_features , w , N , S , m , alpha);
					hamming_Categories.put(x, hsc);
					System.out.println(hamming_Categories.get(x).getEntry(0).size());
				}
				Map<Integer , ActiveSets> active_sets = new HashMap<Integer , ActiveSets>();
				for(Integer x : I_) {
					System.out.println("C size : " + Categories.get(x).getEntry(0) + " , hC size : " + hamming_Categories.get(x).getEntry(0));
//					ActiveSets as = new ActiveSets(x , S , I_ , Categories , hamming_Categories);
//					active_sets.put(x, as);
				}
				break;
			}
			break;
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

}
