package com.z_P3;

import java.util.ArrayList;

	
	
////////////////////////////////////////////////
//
// LTGAEngine is the class responsible for performing  
// all Linkage Tree operations on each level.
//
////////////////////////////////////////////////
	
class LTGAEngine{															 
		
	private ArrayList<Integer>[] 	       clusters;								// 'clusters' stores each cluster as an ArrayList of variables.
	private ArrayList<Integer>[] finalClustersIndex;								// 'finalClustersIndex' stores the indices of all final clusters in 'size buckets'.
	private float[][]       	      pairDistances;								// Matrix of distances between each and every cluster.				
	private int[]                            usable;								// Use this structure to rebuild the linkage tree. 		
																					// NOTE: 'usable' stores the indices of the clusters in the array 'clusters'.
		
	@SuppressWarnings("unchecked") 													// Check: B. Eckel, "Thinking in Java", 4th ed., MindView Inc., 2006, [pp. 759-761]
	public LTGAEngine(){			
		clusters           = new ArrayList[P3Engine.nClusters];
		finalClustersIndex = new ArrayList[Problem.n+1];
		pairDistances      = new float[P3Engine.nClusters][P3Engine.nClusters];
		usable		       = new int[Problem.n];
			
		for(int i = 0; i < P3Engine.nClusters; i++){
			clusters[i] = new ArrayList<Integer>();									// INIT: The first 'n' clusters are singular and in random order.
			if(i < Problem.n)														//       The remaining clusters are empty.
				clusters[i].add(i);
			
			for(int j = 0; j < P3Engine.nClusters; j++)								// INIT: All initial pair distances are -1 (check Population.cpp::28:@Goldman).
				pairDistances[i][j] = -1;
		}						
	}												
		
		
	public float improve(Individual solution, float fitness, ArrayList<Individual> individuals){
		int[] donorIndices = Util.iota(individuals.size(), 0);
			
		float         newFitness = Float.NEGATIVE_INFINITY;
		float currentBestFitness = Float.NEGATIVE_INFINITY;
		ArrayList<Integer> cluster;
		for(int i = 0; i < finalClustersIndex.length; i++){							// NOTE: 'finalClusters' stores indices of clusters in "size buckets".
			Util.shuffle(finalClustersIndex[i]); 
			for(int j = 0; j < finalClustersIndex[i].size(); j++){					// This traverse order implements the "smallest first" cluster ordering.
				int lastIndex = donorIndices.length;								// Use this variable to ensure no donor is chosen twice for the same cluster.
				cluster       = clusters[finalClustersIndex[i].get(j)];
				do{
					int k            = P3.random.nextInt(lastIndex);				// Generate random integer between 0 and lastUnused-1.
					Individual donor = individuals.get(donorIndices[k]);
					newFitness       = donate(solution, fitness, cluster, donor);	// Compute the new fitness. If there is no donation then 'donate' returns NEGATIVE_INFINITY.
					Util.swap(donorIndices, k, lastIndex-1);						// Ensure no donor is chosen twice for the same cluster.
					lastIndex--;											
				}while(lastIndex > 0 && newFitness == Float.NEGATIVE_INFINITY);		// Stop when a donor donates (successfully or not) for this cluster.
				if(newFitness > currentBestFitness)									// or when there is no valid donor in the current population.
					currentBestFitness = newFitness;								// Store current fitness in 'currentBestFitness'. 'newFitness' will be updated in the next donation.
				if(currentBestFitness > fitness)									// The donation made an improvement and 'solution' was changed.
					fitness = currentBestFitness;									// Update 'fitness' accordingly.
			}	
		}
		return currentBestFitness;
	}
			
	private float donate(Individual solution, float fitness, ArrayList<Integer> cluster, Individual donor){
		float newFitness = Float.NEGATIVE_INFINITY;									    // If there is no donation then return NEGATIVE_INFINITY.
		int[]      swaps = new int[cluster.size()];									    // Array of swapped loci. Use 'swaps' and not 'cluster' to revert changes.
		int       nSwaps = 0;												
		boolean  changed = false;
		for(int index: cluster){
			char s = solution.getAllele(index);
			char d = donor.getAllele(index); 
			if(s != d){																	// A donation is going to be made. Set 'changed = true'.
				swaps[nSwaps++] = index;
				changed         = true;
				char temp       = s;													// Swap alleles 's' and 'd'.
				solution.setAllele(index, d);
				donor.setAllele(index, temp);
			}
		}
																							// TODO: Count number of donations, fitness improvements, ties, and failures.
		if(changed){																	// We found a valid donor.
			newFitness = solution.computeFitness();	
			if(newFitness >= fitness)													// Keep changes.
				for(int index = 0; index <  nSwaps; index++)
					donor.setAllele(swaps[index], solution.getAllele(swaps[index]));	// Reconstruct donor. Keep solution changed.
			else															 
				for(int index = 0; index <  nSwaps; index++){							// A donation was made but it resulted in a fitness decline.
					char    s = solution.getAllele(swaps[index]);						// Revert all changes in the solution and the donor.
					char    d = donor.getAllele(swaps[index]); 							
					char temp = s;														
					solution.setAllele(swaps[index], d);								// The new (smaller) fitness value will be returned to signal that a change was made.
					donor.setAllele(swaps[index], temp);								// 'improve(...)' is responsible for updating the new fitness values accordingly.
				}						
		}																				
		return newFitness; 				
	}
	
		
	public void rebuildTree(Individual solution, float fitness, int[][][] occurrences){
		
		initClusterDataStructure(occurrences);											// INIT: clusters, clustersOrder, pairwiseDistances.											
			
		////////////////////////////////////////////
		// Rebuild all clusters after singular ones.
		///////////////////////////////////////////
		int first, second;													// Merging clusters.
		int last, bestIndex;												// Current closest clusters
		int endPath   = 0;													// Track usable clusters in path. Should this be initialized to 1 ??
		int endUsable = usable.length;			
		for(int newC = Problem.n; newC < P3Engine.nClusters; newC++){					
			Util.shuffle(usable, endPath, endUsable);				
			if(endPath == 0)
				endPath++;													// If nothing in the path just add a random usable cluster
			while(endPath < endUsable){					
				last          = usable[endPath - 1];
				bestIndex     = endPath;
				float minDist = pairDistances[last][usable[bestIndex]];
				for(int i = endPath + 1; i < endUsable; i++)
					if(pairDistances[last][usable[i]] < minDist){
						minDist   = pairDistances[last][usable[i]];
						bestIndex = i;
					}					
				if(endPath > 1 && 											// We found one pair that is closest to each other than to any other subset.
				   minDist >= pairDistances[last][usable[endPath-2]])		// Therefore, by convexity, we can stop searching and just merge them.
					break;																																	
				Util.swap(usable, endPath, bestIndex);
				endPath++;					
			}					
			first          = usable[endPath-2];								// The last two elements in the path are the clusters to merge.
			second         = usable[endPath-1];
			int firstSize  = clusters[first].size();
			int secondSize = clusters[second].size();
			endPath -= 2;													// Remove things from the path.
			
			clusters[newC].addAll(clusters[first]);							// Merge the two clusters in the right position.
			clusters[newC].addAll(clusters[second]);
				
			if(Util.floatRound(pairDistances[first][second]) != 0){			// Add the original clusters to the final clusters		
				//if(firstSize > 1)											// Only if their distance is not zero and if they
					finalClustersIndex[firstSize].add(first);					// are not singular clusters.
				//if(secondSize >1)
					finalClustersIndex[secondSize].add(second);
			}
			
			int i = 0;														// Compute all distances to the new cluster
			while(i < endUsable){
				int current = usable[i];
				if(current == first || current == second){					// Remove 'first' and 'second' from usable
					Util.swap(usable, i, endUsable-1);						
					endUsable--;
					continue;												// NOTE: 'i' is NOT incremented in this case.
				}
				float firstDistance  = pairDistances[first][current];
				float secondDistance = pairDistances[second][current];
				firstDistance       *= (float)firstSize;
				secondDistance      *= (float)secondSize;
				pairDistances[current][newC] = (firstDistance + secondDistance)/((float)(firstSize + secondSize));
				pairDistances[newC][current] = pairDistances[current][newC];
				i++;
			}																// NOTE: The new cluster is stored at the end of usable.				 
			usable[endUsable++] = newC;										// With this, the size of usable is effectively reduced by one. 
		}																	// END:  'for(int newC = ...' loop.	     			
	}																		// NOTE: No need for determining the order. 
																			//		 Use 'finalClusters' size buckets to enforce Smallest First ordering.
		
	private void initClusterDataStructure(int[][][] occurrences){
		// Util.shuffle(clusters, 0, Problem.n); 							// NOTE: Is this 'shuffle' necessary? 
		for(int i = Problem.n; i < P3Engine.nClusters; i++)						
			clusters[i] = new ArrayList<Integer>();							// INIT: The first 'n' clusters are singular and in random order.
		for(int i = 0; i < Problem.n-1; i++)								// INIT: Pairwise distances between all initial singular clusters => O(n^2). 
			for(int j = i+1; j < Problem.n; j++){							
				int ci = clusters[i].get(0);								
				int cj = clusters[j].get(0);
				pairDistances[i][j] = computeDistance(ci, cj, occurrences);	// NOTE: Each distance is indexed by (i,j), the indices where the corresponding pair of clusters is stored in the 'clusters' vector.
				pairDistances[j][i] = pairDistances[i][j];					// Keep 'distances' matrix symmetric.
			}
		for(int i = 0; i < Problem.n; i++)						
			usable[i] = i;													// INIT: 'usable' clusters are all singular ones.
		for(int i = 0; i < Problem.n+1; i++)
			finalClustersIndex[i] = new ArrayList<Integer>();				// INIT: The indices of all useful clusters are stored in 'size buckets'.	
	}
	
		
	private float computeDistance(int ci, int cj, int[][][] occurrences){
		int[] partialSums = new int[4];
		int[] pair; 														// NOTE: Matrix 'occurrences' is NOT symmetric but merely upper triangular.
		if(ci < cj)
			pair = occurrences[ci][cj];
		else
			pair = occurrences[cj][ci];
		
		partialSums[0] = pair[0] + pair[2];									// NOTE: i = '0'
		partialSums[1] = pair[1] + pair[3];									// NOTE: i = '1'
		partialSums[2] = pair[0] + pair[1];									// NOTE: j = '0'
		partialSums[3] = pair[2] + pair[3];									// NOTE: j = '1'
		float total = partialSums[0] + partialSums[1];
		float separate = negativeEntropy(partialSums, total);
		float together = negativeEntropy(pair, total);
		if(together != 0)
			return (float)2 - separate/together;
		else
			return 0;			
	}
	
	private float negativeEntropy(int[] counts, float total){
		float sum = 0;
		float p;
		for(int k = 0; k < 4; k++)
			if(counts[k] != 0){
				p    = (float)counts[k] / total;
				sum += (p * Math.log(p));
			}
		return sum;
	}
	
} // END: LTGAEngine class.









