package edu.salisbury.photonic.log_analyisis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.salisbury.photonic.core_simulator.Coordinate;
import edu.salisbury.photonic.core_simulator.CoreLog;
import edu.salisbury.photonic.core_simulator.LogEntry;
import edu.salisbury.photonic.core_simulator.SortingHelper;


/**
 * A subclass of {@link Analyzer} which can be used to evaluate the number
 * of times each core has sent or received data based on a log-file 
 * represented by a {@link CoreLog} object.
 * @author timfoil
 *
 */
public class CumulativeIOAnalyzer extends Analyzer 
{
	/**
	 * Constructs a CumulativeIOAnalyzer. With resultEntriesPerRow equal to 2.
	 */
	public CumulativeIOAnalyzer()
	{
		resultDescription = "Cores that are communicating the most (combined IO operations)";
	}
	
	/**
	 * Constructor for CumulativeIOAnalyzer
	 * @param resultEntriesPerRow for the resulting {@code String} of the experiment
	 */
	public CumulativeIOAnalyzer(int resultEntriesPerRow)
	{
		this.resultEntriesPerRow = resultEntriesPerRow;
		resultDescription = "Cores that are communicating the most (combined IO operations)";
	}
	
	@Override
	public String analyze(CoreLog log) 
	{
		HashMap<Coordinate,Integer> sentMessages = new HashMap<>();
		
		for(int i = 0; i < log.logSize(); i++)
		{
			LogEntry entry = log.getEntry(i);
			
			Coordinate destCoord = new Coordinate(entry.destX(), entry.destY());
			
			//If the key already exists update the value, timesOccured
			//otherwise put a new entry in for the key
			if(sentMessages.containsKey(destCoord))
			{
				int timesOccured = sentMessages.remove(destCoord);
				sentMessages.put(destCoord, ++timesOccured);
			} 
			else 
			{
				sentMessages.put(destCoord, 1);
			}
			
			Coordinate sourceCoord = new Coordinate(entry.sourceX(), entry.sourceY());
			
			//If the key already exists update the value, timesOccured
			//otherwise put a new entry in for the key
			if(sentMessages.containsKey(sourceCoord))
			{
				int timesOccured = sentMessages.remove(sourceCoord);
				sentMessages.put(sourceCoord, ++timesOccured);
			} 
			else 
			{
				sentMessages.put(sourceCoord, 1);
			}
			
		}
		
		List<Map.Entry<Coordinate, Integer>> sortedList = 
				SortingHelper.SortHashMapByValue(sentMessages);//analyze sentMessages
		return sortMapEntriesByDescendingValue(sortedList);
	}

}
