package edu.utexas.cs.nn.evolution.mutation.tweann;

import edu.utexas.cs.nn.evolution.genotypes.TWEANNGenotype;
import edu.utexas.cs.nn.parameters.CommonConstants;

/**
 *Mutates all fully connected modules
 * @author Jacob Schrum
 */
public class FullyConnectedModuleMutation extends ModuleMutation {

	/**
	 * Default constructor
	 */
	public FullyConnectedModuleMutation() {
		//Command line parameter, "Mutation rate for mode mutation that connects to all inputs"
		super("fullMMRate");
	}

	/**
	 * determines whether or not to perform FCMMmutation
	 */
	@Override
	public boolean perform() {
		return !CommonConstants.fs && super.perform();
	}

	/**
	 * adds a fully connected and mutated module
	 */
	@Override
	public void addModule(TWEANNGenotype genotype) {
		int linksAdded = genotype.fullyConnectedModeMutation();
		int[] subs = new int[linksAdded];
		for (int i = 0; i < linksAdded; i++) {
			subs[i] = i + 1;
		}
		cullForBestWeight(genotype, subs);
	}
}
