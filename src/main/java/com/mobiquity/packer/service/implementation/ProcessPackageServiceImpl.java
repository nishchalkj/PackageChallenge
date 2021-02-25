package com.mobiquity.packer.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.PackerOutputResponse;
import com.mobiquity.packer.service.ProcessPackageService;
import com.mobiquity.packer.util.Packer;

/**
 * This is the service layer implementation class for the API
 * 
 * @author Nishchal
 *
 */
@Service
public class ProcessPackageServiceImpl implements ProcessPackageService {

	@Autowired
	private PackerOutputResponse outputResponse;

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
	@Override
	public PackerOutputResponse processPackage(String filePath) throws APIException {
		List<String> indexesList = new ArrayList<>();
		String finalizedPackages = Packer.pack(filePath);
		String[] indexes = finalizedPackages.split("[()]");
		for (int i = 0; i < indexes.length; i++) {
			if (!StringUtils.isBlank(indexes[i])) {
				indexesList.add(indexes[i]);
			}
		}
		outputResponse.setIndexes(indexesList);
		return outputResponse;
	}

}
