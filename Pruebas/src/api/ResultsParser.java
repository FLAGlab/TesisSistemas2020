package api;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.stream.IntStream;

public class ResultsParser {

	public static void main(String[] args) throws IOException {
		//LENGTH,num
		//VERTEX,num
		//algorithm,tasks,iteration,thread,tasks,time
		String baseDir = System.getProperty("user.dir");
		baseDir = baseDir.substring(0, baseDir.lastIndexOf("/"));
		int[] cores = {10, 16, 24, 48, 90};
		for(int i : cores) {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(baseDir + "out_"+i+".csv"), StandardCharsets.UTF_8));
			//Iterations times
			PrintWriter prioritiesTimes = new PrintWriter(new FileWriter(baseDir + "/priorities_times.csv"));
			PrintWriter fifoTimes = new PrintWriter(new FileWriter(baseDir + "/fifo_times.csv"));
			PrintWriter lifoTimes = new PrintWriter(new FileWriter(baseDir + "/lifo_times.csv"));
			PrintWriter priorities;
			PrintWriter fifo;
			PrintWriter lifo;
			boolean length = false;

			String line = "";
			int size = 0;
			int iterationCount = 0;
			double priorityTime = 0.0;
			Hashtable<Integer, double[]> priorityData = new Hashtable<Integer, double[]>();
			while((line=br.readLine())!=null && line.length()!=0) {
				String[] parts = line.split(",");
				if(!length && parts[0].equals("LENGTH")) {
					size = Integer.parseInt(parts[1]);
					fifo = new PrintWriter(new FileWriter(baseDir + "fifo_" + size + ".csv"));
					lifo = new PrintWriter(new FileWriter(baseDir + "lifo_" + size + ".csv"));
					priorities = new PrintWriter(new FileWriter(baseDir + "priorities_" + size + ".csv"));
					//prioritiesTimes.println(size + "," + priorityTime / iterationCount);
					iterationCount = 0;
					size = 0;
					length = true;
				} else if(parts[0].equals("VERTEX")) {
					iterationCount += Double.parseDouble(parts[1]);
					iterationCount ++;
				} else {
					//parse per method - iteration - p.id and average
					if(parts[0].equals("PRIO")) {
						int tasks = Integer.parseInt(parts[1]);
						double[] values = priorityData.get(tasks);
						if(values == null) 
							priorityData.put(tasks, IntStream.rangeClosed(0,5).mapToDouble(it -> 0.0).toArray());
						else {
							values[Integer.parseInt(parts[3])] +=  Double.parseDouble(parts[5]);
							priorityData.replace(100, values);
						}
					} else if(parts[0].equals("FIFO")) {

					} else { //LIFO

					}
					iterationCount ++;
				}			
			}
		}
	}
}
