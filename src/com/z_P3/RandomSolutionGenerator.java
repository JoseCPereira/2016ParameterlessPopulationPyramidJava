package com.z_P3;


public class RandomSolutionGenerator{
	
	public static Individual generate(){
		char[] genotype = new char[Problem.n];
		for(int i = 0; i < Problem.n; i++)
			genotype[i] = P3.random.nextBoolean()? '0' : '1';
		return new Individual(genotype);
	}
	
}