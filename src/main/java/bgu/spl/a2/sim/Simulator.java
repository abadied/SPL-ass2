/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.google.gson.Gson;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	/**
	* Begin the simulation
	* Should not be called before attachWorkStealingThreadPool()
	*/
    public static ConcurrentLinkedQueue<Product> start(){
    	return null; // TODO
    }
	
	/**
	* attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	* @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
	*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){
		
	}
	
	public static void main(String [] args){
		
		Gson gson = new Gson();
		InputParse input = null;
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]));){
			input = gson.fromJson(br, InputParse.class);
		} catch (IOException e) {
			System.out.println("failed to read file");
		}
		
		if (input == null) {
			System.out.println("error reading file");
			System.exit(1);
		}
		
		//WorkStealingThreadPool pool = new WorkStealingThreadPool(input.nthreads);
		int a=2;
	}
	
	private class InputParse {
		int nthreads;
		Tool[] tools;
		ManufactoringPlan[] plans;
		Wave waves;
	}
	
	private class Wave {
		Product product;
		int qty;
		int startId;
	}
}
