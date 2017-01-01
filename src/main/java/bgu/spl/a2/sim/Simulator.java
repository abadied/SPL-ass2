/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.BuildProductTask;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	
	static WorkStealingThreadPool pool;
	static GsonReader input;
	static Warehouse warehouse;
	static AtomicInteger finished;
	
	static Object lock;
	
	/**
	* Begin the simulation
	* Should not be called before attachWorkStealingThreadPool()
	*/
    public static ConcurrentLinkedQueue<Product> start(){
    	lock = new Object();
    	pool.start();
    	LinkedList<Deferred<Product>> dProducts = new LinkedList<>();
    	int qty = 0;
    	finished = new AtomicInteger(0);
    	for (GsonReader.Zerg[] wave: input.waves) {
			for (GsonReader.Zerg zerg : wave) {
				for (int i = 0; i < zerg.qty; i++) {
					BuildProductTask task = new BuildProductTask(zerg.startId + i, zerg.product, warehouse);
					pool.submit(task);
					task.getResult().whenResolved(() -> reportFinished()); // counts how many zergs are done
					dProducts.add(task.getResult()); // save a deferred object for the product
					qty++;
				}
			}
			waitForWave(qty);
		}
    	
    	// collect the deferred to a queue of products
    	ConcurrentLinkedQueue<Product> products = new ConcurrentLinkedQueue<>();
    	for(Deferred<Product> dProd : dProducts) 
    		products.add(dProd.get());
    	
    	return products;
    }
    
    /**
     * handles the counting of finished zergs and notifies the main function 
     */
    private static void reportFinished() {
    	synchronized (lock) {
    		finished.incrementAndGet();
    		lock.notify();
		}
    }
    
    /**
     * waits for the wave to finish
     * @param qty expected number of zergs
     */
    private static void waitForWave(int qty) {
    	synchronized(lock) {
			try {
				while(finished.get() < qty)
					lock.wait();
			} catch (InterruptedException e){
				System.out.println("interrupted");
				System.exit(1);
			}
		}
    }
    
	/**
	* attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	* @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
	*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){
		pool = myWorkStealingThreadPool;
	}
	
	public static void main(String [] args){
		
		//// Read JSON file into GsonReader class
		Gson gson = new Gson();
		input = null;
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]));){
			input = gson.fromJson(br, GsonReader.class);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		if (input == null) {
			System.out.println("error reading file");
			System.exit(1);
		}
		
		//// Create Pool
		Simulator.attachWorkStealingThreadPool(new WorkStealingThreadPool(input.threads));
		
		//// Create Tools
		warehouse = new Warehouse();
		for (GsonReader.ToolInfo tooli: input.tools){
			switch (tooli.tool){
			case "gs-driver":
				warehouse.addTool(new GcdScrewDriver(), tooli.qty);
				break;
			case "np-hammer":
				warehouse.addTool(new NextPrimeHammer(), tooli.qty);
				break;
			case "rs-pliers":
				warehouse.addTool(new RandomSumPliers(), tooli.qty);
				break;
			}
		}
			
		//// Create Plans
		for (GsonReader.Plan plan: input.plans)
			warehouse.addPlan(new ManufactoringPlan(plan.product, plan.parts, plan.tools));
		
		//// start the simulator
		ConcurrentLinkedQueue<Product> SimulationResult = Simulator.start();
		
		try {
			pool.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		////// TODO: remove before sending ///////////////////////////////////////////////////
		
		Warehouse wh = warehouse;
		System.out.println("deferred waiting for resolve: " + (wh.gsd_deferreds.size() + wh.nph_deferreds.size() + wh.rsp_deferreds.size()));
		System.out.println("tools avaiable: " + (wh.gsDrivers.size() + " gsd, " + wh.npHammers.size() + " nph, " + wh.rsPliers.size() + " rsp"));
		
		System.out.println("\nConstruction Complete");
		System.out.println("Creating txt file -------- dont forget to remove this");
		
		File txtfout = new File("out.txt");
		
		try (FileOutputStream fos = new FileOutputStream(txtfout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {
			for (Product p : SimulationResult) {
				bw.write(p.getFinalId() + "");
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		/////////////////////////////////////////////////////////////////////////////////////
		
		
		//// write the result.ser file
		try (FileOutputStream fout = new FileOutputStream("result.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fout);) {
			oos.writeObject(SimulationResult);
		}
		catch(IOException e){
			e.printStackTrace();
		}
				
	}
}
