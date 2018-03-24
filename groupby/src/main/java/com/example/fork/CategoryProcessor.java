package com.example.fork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import com.example.dto.Category;
import com.example.dto.Product;

public class CategoryProcessor extends RecursiveTask<Map<Category, Integer>> {
	private static final long serialVersionUID = 1L;
	List<Product> products;

	public CategoryProcessor(List<Product> products) {
		this.products = products;
	}

	@Override
	protected Map<Category, Integer> compute() {
		List<CategoryProcessor> tasks = new ArrayList<CategoryProcessor>();
		Map<Category, Integer> resultMap = new HashMap<Category, Integer>();
		int size = products.size();
		if (size == 1) {
			Category key = products.get(0).getCategory();
			Integer value = resultMap.get(key);
			value = value == null ? 0 : value;
			resultMap.put(key, value + 1);
		} else {
			int mid = size / 2;
			mid = mid == 0 ? 1 : mid;

			CategoryProcessor task = new CategoryProcessor(products.subList(0, mid));
			task.fork();
			tasks.add(task);
			task = new CategoryProcessor(products.subList(mid, size));
			task.fork();
			tasks.add(task);
		}

		addResultsFromTasks(resultMap, tasks);
		
		return resultMap;
	}

	private void addResultsFromTasks(Map<Category, Integer> resultMap, List<CategoryProcessor> tasks) {
		for (CategoryProcessor item : tasks) {
			Map<Category, Integer> map = item.join();
			for (Category cate : map.keySet()) {
				Integer value = resultMap.get(cate);
				value = value == null ? map.get(cate) : value + map.get(cate);
				resultMap.put(cate, value);
			}
		}
	}

}