package boardGame.heuristics;

import java.util.List;

import boardGame.BoardGameState;
import edu.utexas.cs.nn.networks.Network;
import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.util.MiscUtil;

public class NNBoardGameHeuristic<T extends Network, S extends BoardGameState> implements BoardGameHeuristic<S> {

	T network;
	
	public NNBoardGameHeuristic(T net){
		network = net;
	}
	
	public NNBoardGameHeuristic(){ // Used as a blank constructor; the Network can be set in the BoardGameTasks
	}
	
	@Override
	public double heuristicEvalution(S current) {
		if(Parameters.parameters.booleanParameter("stepByStep")){
			System.out.print("Press enter to continue");
			System.out.println(current);
			MiscUtil.waitForReadStringAndEnterKeyPress();
		}
		network.flush(); // wipe out recurrent activations
		
		if(Parameters.parameters.booleanParameter("heuristicOverrideTerminalStates")){ // Overrides the Network's evaluation if set to True
			if(current.endState()){
				List<Integer> winners = current.getWinners();
				
				if(winners.size() == 1 && winners.contains(current.getCurrentPlayer())){ // Current Player is the only winning Player
					return 1;
				}else if(winners.size() > 1 && winners.contains(current.getCurrentPlayer())){ // More than one Player wins, but contains Current Player; considered a Tie
					return 0;
				}else{ // Undisputed Loss; Current Player does not win at all
					return -1;
				}
				
			}else{
				return network.process(current.getDescriptor())[0]; // Returns the Network's Score for the current BoardGameState's descriptor
			}
		}
		
		return network.process(current.getDescriptor())[0]; // Returns the Network's Score for the current BoardGameState's descriptor
		
	}

}