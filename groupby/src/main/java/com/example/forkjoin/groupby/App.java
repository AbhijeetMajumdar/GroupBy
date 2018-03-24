package com.example.forkjoin.groupby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.example.dto.Category;
import com.example.dto.Product;
import com.example.fork.CategoryProcessor;

public class App {
	static List<Product> buildProducts(int size) {
		Random random = new Random();
		List<Product> list = new ArrayList<Product>(size);
		for (int i = 0; i < size; i++) {
			Product p = new Product();
			p.setId(i);
			p.setCategory(random.nextInt() % 2 == 0 ? Category.Commerical
					: random.nextInt() % 5 == 0 ? Category.Domestic : Category.Millitry);
			list.add(p);
		}
		return list;
	}

	public static void main(String[] args) {
		// Create ForkJoinPool using the default constructor.
		ForkJoinPool pool = new ForkJoinPool();
		// Create three FolderProcessor tasks. Initialize each one with a different
		// folder path.
		CategoryProcessor system = new CategoryProcessor(buildProducts(100001));
		CategoryProcessor apps = new CategoryProcessor(buildProducts(200003));
		CategoryProcessor documents = new CategoryProcessor(buildProducts(300007));
		// Execute the three tasks in the pool using the execute() method.
		pool.execute(system);
		pool.execute(apps);
		pool.execute(documents);
		// Write to the console information about the status of the pool every second
		// until the three tasks have finished their execution.
		do {
			System.out.printf("******************************************\n");
			System.out.printf("** : Parallelism: %d\n", pool.getParallelism());
			System.out.printf("** :  Active Threads: %d\n", pool.getActiveThreadCount());
			System.out.printf("** :  Task Count: %d\n", pool.getQueuedTaskCount());
			System.out.printf("** :  Steal Count: %d\n", pool.getStealCount());
			System.out.printf("******************************************\n");
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while ((!system.isDone()) || (!apps.isDone()) || (!documents.isDone()));
		// Shut down ForkJoinPool using the shutdown() method.
		pool.shutdown();
		// Write the number of results generated by each task to the console.
		Map<Category, Integer> results;
		results = system.join();
		System.out.printf("System: %s files found.\n", results);
		results = apps.join();
		System.out.printf("Apps: %s files found.\n", results);
		results = documents.join();
		System.out.printf("Document: %s files found.\n", results);
	}
}