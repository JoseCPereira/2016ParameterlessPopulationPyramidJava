package com.z_P3;

import java.util.Arrays;

public class Individual {
	private char[] individual = new char[Problem.n];
	
	public Individual(){}
	public Individual(Individual johnDoe){this.individual = johnDoe.getIndividual();}		
	public  Individual(char[] individual){this.individual = individual;}					// NOTE: shallow copy of individual
	public  Individual(String individual){this.individual = individual.toCharArray();}
	
	public char  getAllele(int j){return this.individual[j];}
	public char[] getIndividual(){return this.individual;}
	
	public void 		setAllele(int j, char c){this.individual[j] = c;}
	public void setIndividual(char[] individual){this.individual    = individual;}
	
	public void flipBit(int index){
		this.individual[index] = (char)(((int)this.individual[index] - 47)%2 + 48);
	}

	
	public float computeFitness(){return P3Engine.problem.computeFitness(this);}
	
	
	public Individual copyIndividual(){
		char[] copy = new char[Problem.n];
		for(int i = 0; i < Problem.n; i++){
			char c = individual[i];
			copy[i] = c;
		}
		return new Individual(copy);
	}
	
	public char[] copyIndividualLoci(){
		char[] copy = new char[Problem.n];
		for(int i = 0; i < Problem.n; i++){
			char c = individual[i];
			copy[i] = c;
		}
		return copy;
	}	
		
	public int distance(Individual johnDoe){										// Hamming Distance
		char[] thatIndividual = johnDoe.getIndividual();
		int dist = 0;
		for(int i = 0; i < Problem.n; i++)
			if(this.individual[i] != thatIndividual[i])
				dist++;
		return dist;
	}
	
	public boolean isZero(){
		for(int i = 0; i < Problem.n; i++)
			if(individual[i] != '0')
				return false;
		return true;
	}
	
	public boolean isOne(){
		for(int i = 0; i < Problem.n; i++)
			if(individual[i] != '1')
				return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(individual);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Individual))
			return false;
		Individual other = (Individual) obj;
		if (!Arrays.equals(individual, other.individual))
			return false;
		return true;
	}

	
	public String toString(){
		String str = "";
		for(int i = 0; i < Problem.n; i++)
			str += individual[i];
		return str;
	}
}
