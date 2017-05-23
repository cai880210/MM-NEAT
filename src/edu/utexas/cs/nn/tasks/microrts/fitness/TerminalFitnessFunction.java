package edu.utexas.cs.nn.tasks.microrts.fitness;

import java.util.List;

import edu.utexas.cs.nn.util.datastructures.Pair;
import micro.rts.GameState;
import micro.rts.PhysicalGameState;
import micro.rts.units.Unit;

public class TerminalFitnessFunction extends RTSFitnessFunction{
	
	public TerminalFitnessFunction(){}

	/**
	 * scores performance in a game
	 * @param terminalGameState
	 * @return pair of double[], the first of which has {victory, time, unitDifference}
	 */
	public Pair<double[], double[]> getFitness(GameState terminalGameState) {
		Pair<double[], double[]> score = new Pair<>(new double[3], new double[0]); 
		//first[]:{victory, time, unitDifference, } on a scale from -1 to 1, except unit difference, which starts at 0 and can go up or down
		
		pgs = terminalGameState.getPhysicalGameState();
		int gameEndTime = terminalGameState.getTime();
		List<Unit> unitsLeft = terminalGameState.getUnits();

		if(terminalGameState.winner() == 0){ //victory organism being tested! 
			score.t1[0] = 1;
			score.t1[1] = (double) (maxCycles - gameEndTime) / maxCycles * RESULTRANGE - 1; //lower time is better
			for(Unit u : unitsLeft){
				if(u.getType().name != "Resource") score.t1[2] += u.getType().cost;
			}
		} else if(terminalGameState.winner() == 1){ //defeat for organism being tested
			score.t1[0] = -1;
			score.t1[1] = (double) (maxCycles - gameEndTime) / maxCycles * -1 * RESULTRANGE + 1; //holding out for longer is better
			for(Unit u : unitsLeft){
				if(u.getType().name != "Resource") score.t1[2] -= u.getType().cost;
			}
		} else if(terminalGameState.winner() == -1){ //tie, ran out of time
			score.t1[0] = 0;
			for(Unit u : unitsLeft){
				if(u.getPlayer()==0) score.t1[2] += u.getType().cost;
				else if(u.getPlayer()==1) score.t1[2] -= u.getType().cost;
			}
			//if winning hard but didn't close out => very bad, if basically even => okay, if losing but held out => great
			score.t1[1] = -1 * score.t1[2] / (pgs.getHeight()*pgs.getWidth());  
		}
		System.out.println("result?: "+score.t1[0] + " unit-difference: "+ score.t1[2] + " time: " +score.t1[1]);
		return score;
	} //END fitnessFunction

	@Override
	public String[] getFunctions() {
		return new String[]{"win/loss","time","unit-difference"};
	}

}