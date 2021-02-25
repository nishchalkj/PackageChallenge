package com.mobiquity.packer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.Item;

/**
 * This is the utility class for the packer class
 * 
 * @author Nishchal
 *
 */
public class Packer {
	

	private static final Logger log = LoggerFactory.getLogger(Packer.class);

	private Packer() throws InstantiationException {
		throw new InstantiationException("Cannot instantiate this class");
	}

	/**
	 * This method receives the file path and reads the data from the file and
	 * returns the response
	 * 
	 * @param filePath
	 *            path to the file
	 * @return indexes of items in each package
	 * @throws APIException
	 *             custom exception
	 */
	public static String pack(String filePath) throws APIException {
		Map<String, List<Item>> packages = new LinkedHashMap<>();
		File initialFile = new File(filePath);
		
		try (InputStream inputStream = new FileInputStream(initialFile.getAbsolutePath())) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				if (!StringUtils.isBlank(line)) {
					readRow(line, packages);
				}
			}
			String val = getFinalizedPackages(packages);
			return getFinalizedPackages(packages);
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw new APIException("IO exception while reading the file");
		}
	}

	/**
	 * This method reads each line from the file and sets the result in map for
	 * further processing
	 * 
	 * @param line
	 *            each line from the file
	 * @param packages
	 *            map containing values of every line as key value pair{Key is
	 *            package weight, Value is different items}
	 */
	private static void readRow(String line, Map<String, List<Item>> packages) {

		List<Item> allItems = new ArrayList<>();
		String[] eachLine = line.split(":");
		String[] items = eachLine[1].split("[()]");
		for (int i = 0; i < items.length; i++) {
			String item = items[i];
			if (!StringUtils.isBlank(item)) {
				String[] itemSplit = item.split(",");
				Item eachItem = new Item();
				eachItem.setIndex(itemSplit[0]);
				eachItem.setWeight(itemSplit[1]);
				eachItem.setPrice(itemSplit[2]);
				allItems.add(eachItem);
			}
		}
		packages.put(eachLine[0], allItems);
	}

	/**
	 * This method iterates over the input map and gets the indexes of items
	 * which can be put in respective packages
	 * 
	 * @param packages
	 *            map containing values of every line as key value pair{Key is
	 *            package weight, Value is different items}
	 * @return indexes of items in each package
	 */
	private static String getFinalizedPackages(Map<String, List<Item>> packages) {
		StringBuilder output = new StringBuilder();
		Map<String, List<Item>> finalResult = new LinkedHashMap<>();
		for (Map.Entry<String, List<Item>> mapElement : packages.entrySet()) {
			iterateEachPackage(mapElement.getKey(), mapElement.getValue(), finalResult);
		}
		for (Map.Entry<String, List<Item>> mapElement : finalResult.entrySet()) {
			List<Item> items = mapElement.getValue();
			if (null != items) {
				output.append("(");
				int counter = 0;
				for (Item item : items) {
					if (counter == 0) {
						output.append(item.getIndex());
					} else {
						output.append(",").append(item.getIndex());
					}
					counter++;
				}
				output.append(")");
			} else {
				output.append("(" + "-" + ")");
			}
		}
		log.info(output.toString());
		return output.toString();
	}

	/**
	 * This method gets the data of each package and sorts in descending order
	 * and compares every item and select items so the it has maximum cost and
	 * less weight.
	 * 
	 * @param key
	 *            Maximum weight a package can take
	 * @param items
	 *            different items in a package
	 * @param finalResult
	 *            each package data with maximum weight and respective items
	 * @return map containing values of indexes of items which can be put in for
	 *         every package
	 */
	private static Map<String, List<Item>> iterateEachPackage(String key, List<Item> items,
			Map<String, List<Item>> finalResult) {

		Item initialMaxCostItem = null;
		Item maxPriceItem;
		List<Item> finalList = null;
		double packageWeight = Double.parseDouble(key.trim());
		Collections.sort(items, sortByPrice);
		for (int i = 0; i < items.size(); i++) {
			double eachItemWeight = Double.parseDouble(items.get(i).getWeight().trim());
			if (eachItemWeight <= packageWeight) {
				initialMaxCostItem = items.get(i);
				break;
			}
		}
		if (null != initialMaxCostItem) {
			maxPriceItem = comparePrice(initialMaxCostItem, items);
			log.info(maxPriceItem.getPrice() + " " + maxPriceItem.getWeight());
			finalList = selectItemsInPackage(packageWeight, maxPriceItem, items);
			Collections.sort(finalList, sortByIndex);
		} else {
			log.info("No Items in package:" + " " + key);
		}

		finalResult.put(key, finalList);
		return finalResult;
	}

	/**
	 * This method compares the weight of every items on sorted list select
	 * items which are having very less weight and maximum cost
	 * 
	 * @param packageWeight
	 *            Maximum weight a package can take
	 * @param maxPriceItem
	 *            Item with maximum cost in each package
	 * @param items
	 *            list containing items of respective package
	 * @return returns items which can be added in a package
	 */
	private static List<Item> selectItemsInPackage(double packageWeight, Item maxPriceItem, List<Item> items) {
		List<Item> finalList = new ArrayList<>();
		List<Item> intermediateList = new ArrayList<>();
		double itemWeight = Double.parseDouble(maxPriceItem.getWeight().trim());
		for (int i = 0; i < items.size(); i++) {
			if (!maxPriceItem.getIndex().equals(items.get(i).getIndex())
					&& (itemWeight + Double.parseDouble(items.get(i).getWeight().trim()) <= packageWeight)) {
				intermediateList.add(items.get(i));
			}
		}
		if (!intermediateList.isEmpty()) {
			finalList.add(intermediateList.get(0));
		}
		finalList.add(maxPriceItem);
		return finalList;
	}

	/**
	 * This method compares cost of every item with the item with cost so that
	 * If any item with same cost is found then the item with less wait is
	 * selected in further steps
	 * 
	 * @param initialMaxCostItem
	 *            Item with maximum cost in each package
	 * @param items
	 *            list containing items of respective package
	 * @return return item with maximum cost and less wait
	 */
	private static Item comparePrice(Item initialMaxCostItem, List<Item> items) {
		double initialMaxCost = Double.parseDouble(initialMaxCostItem.getPrice().substring(1));
		List<Item> maxCostList = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			if (Double.compare(initialMaxCost, Double.parseDouble(items.get(i).getPrice().substring(1))) == 0) {
				maxCostList.add(items.get(i));
			}
		}
		Collections.sort(maxCostList, sortByWeight);
		return maxCostList.get(0);
	}

	/**
	 * Sorts the items in descending order of the price
	 */
	private static Comparator<Item> sortByPrice = new Comparator<Item>() {
		@Override
		public int compare(Item i1, Item i2) {
			Integer price1 = Integer.parseInt(i1.getPrice().substring(1));
			Integer price2 = Integer.parseInt(i2.getPrice().substring(1));
			return price2 - price1;
		}
	};

	/**
	 * Sorts the items in ascending order of the index
	 */
	private static Comparator<Item> sortByIndex = new Comparator<Item>() {
		@Override
		public int compare(Item i1, Item i2) {
			Integer index1 = Integer.parseInt(i1.getIndex());
			Integer index2 = Integer.parseInt(i2.getIndex());
			return index1 - index2;
		}
	};
	/**
	 * Sorts the items in ascending order of the weight
	 */
	private static Comparator<Item> sortByWeight = new Comparator<Item>() {
		@Override
		public int compare(Item i1, Item i2) {
			double weight1 = Double.parseDouble(i1.getWeight().trim());
			double weight2 = Double.parseDouble(i2.getWeight().trim());
			return Double.compare(weight1, weight2);
		}
	};

}
