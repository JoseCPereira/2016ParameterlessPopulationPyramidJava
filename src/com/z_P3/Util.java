package com.z_P3;

import java.util.ArrayList;





////////////////////////
//
// Utilities class.
//
///////////////////////


public class Util{
	
	public  static float precision = 65536;								// Maximum precision required for comparing two numbers.
	                                                                    // NOTE: Goldman uses 'precision = 65536' @Default.cfg
	public static float floatRound(float value){
		return Math.round(value * precision) / precision;
	}
	
	public static int[] iota(int n, int start){
		int[] options = new int[n];
		for(int i = 0; i < n; i++)
			options[i] = start + i;
		return options;
	}
	
	public static int[] intToBinary(int n, int k){						// Convert integer 'n' to binary array with 'k' bits.
		int[] binary = new int[k];										// NOTE: Naturally, we must have:			
		for(int i = 1; i <= k; i++)										//       0 <= n <= 2^(k-1)
			binary[k-i] = ((n >> (k-i)) & 1);
		return binary;
		
	}
	
	public static int[] shuffle(int n){									// Return the array 0, 1, ..., n-1 in random order.
		int[] numbers = new int[n];
		for(int i = 0; i < n; i++)
			numbers[i] = i;
		for(int i = 1; i < numbers.length; i++){
			int r = i + P3.random.nextInt(numbers.length - i);
			Util.swap(numbers, i-1, r);
		}
		return numbers;
	}
	
	public static void shuffle(int[] numbers){							// Randomly shuffle an array of 'int'.
		for(int i = 1; i < numbers.length; i++){
			int r = i + P3.random.nextInt(numbers.length - i);
			Util.swap(numbers, i-1, r);
		}
	}
	
	public static void shuffle(int[] numbers, int start, int end){		// Randomly shuffle an array of 'int' 
		for(int i = start+1; i < end; i++){								// from 'start' (inclusive) to 'end' (exclusive).
			int r = i + P3.random.nextInt(end - i);
			Util.swap(numbers, i-1, r);
		}
	}
	
	
	public static void swap(int[] numbers, int i, int j){
		int temp   = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
	}
	
	
	public static void shuffle(ArrayList<Integer> numbers){							// Randomly shuffle an array of 'int'.
		for(int i = 1; i < numbers.size(); i++){
			int r = i + P3.random.nextInt(numbers.size() - i);
			Util.swap(numbers, i-1, r);
		}
	}
	
	
	public static void swap(ArrayList<Integer> numbers, int i, int j){
		int temp   = numbers.get(i);
		numbers.set(i, numbers.get(j));
		numbers.set(j, temp);
	}
	
	
	public static void shuffle(ArrayList<Integer>[] numbers, int start, int end){	// Randomly shuffle an array of 'int'.
		for(int i = start+1; i < end; i++){											// from 'start' (inclusive) to 'end' (exclusive).
			int r = i + P3.random.nextInt( end - i);
			Util.swap(numbers, i-1, r);
		}
	}
	
	
	public static void swap(ArrayList<Integer>[] numbers, int i, int j){
		ArrayList<Integer> temp   = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
	}
	
}











