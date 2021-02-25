package com.mobiquity.packer.model;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * This class holds the indexes of the items in each package 
 * @author Nishchal
 *
 */
@Component
public class PackerOutputResponse {
	
	List<String> indexes;

	/**
	 * @return the indexes
	 */
	public List<String> getIndexes() {
		return indexes;
	}

	/**
	 * @param indexes the indexes to set
	 */
	public void setIndexes(List<String> indexes) {
		this.indexes = indexes;
	}
	
	

}
