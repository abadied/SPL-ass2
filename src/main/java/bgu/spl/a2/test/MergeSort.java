/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MergeSort extends Task<int[]> {

	private final int[] array;
	private int length;
	MergeSort ms1;
	MergeSort ms2;

	public MergeSort(int[] array) {
		this.array = array;
		this.length = array.length;
	}

	@Override
	protected void start() {
		if (length > 1){
			int[] arr1 = new int[length/2];
			int[] arr2 = new int[length - length/2];
			
			for (int i = 0; i < length; i++){
				if (i < length / 2)
					arr1[i] = array[i];
				else 
					arr2[i - (length / 2)] = array[i];
			}
			
			ms1 = new MergeSort(arr1);
			ms2 = new MergeSort(arr2);
			
			spawn(ms1,ms2);
			ArrayList<MergeSort> tasksArr = new ArrayList<>();
			tasksArr.add(ms1);
			tasksArr.add(ms2);
			whenResolved(tasksArr, () -> {
					mergeParts();
					complete(array);
				});
		}
		else {
			complete(array);
		}
	}

	private void mergeParts() {
		int i = 0,j = 0;
		int[] arr1 = ms1.getResult().get();
		int[] arr2 = ms2.getResult().get();
		
		for (int k = 0; k < length; k++){
			if (i >= arr1.length)
				array[k] = arr2[j++];
			else if (j >= arr2.length)
				array[k] = arr1[i++];
			else if (arr1[i] < arr2[j])
				array[k] = arr1[i++];
			else
				array[k] = arr2[j++];
		}
	}
    

	
	public static void main(String[] args) throws InterruptedException {
		WorkStealingThreadPool pool = new WorkStealingThreadPool(10);
		int n = 2_000_000; // you may check on different number of elements if you
							// like
		int[] array = new Random().ints(n).toArray();
		
		MergeSort task = new MergeSort(array);
		CountDownLatch l = new CountDownLatch(1);
		pool.start();
		pool.submit(task);
		task.getResult().whenResolved(() -> {
			// warning - a large print!! - you can remove this line if you wish
			//System.out.println(Arrays.toString(task.getResult().get()));
			l.countDown();
		});
		
		l.await();
		
		pool.shutdown();
		
		int[] arr = task.getResult().get();
		
		//checks if the array is really sorted
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1){
				System.out.println("GOOD!");
			}
			else if (arr[i] > arr[i+1]){
				System.out.println("BAD! you failed!");
				break;
			}
		}
		
		
	}

}
