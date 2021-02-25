package com.mobiquity.packer.service;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.PackerOutputResponse;

/**
 * This is the interface for the service layer implementation
 * 
 * @author Nishchal
 *
 */
public interface ProcessPackageService {

	/**
	 * This method receives the filepath and returns the indexes of items in
	 * each package
	 * 
	 * @param filePath
	 *            path to the file
	 * @return indexes of items in each package
	 * @throws APIException
	 *             custom exception
	 */
	public PackerOutputResponse processPackage(String filePath) throws APIException;

}
