package com.mobiquity.packer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.PackerInputRequest;
import com.mobiquity.packer.model.PackerOutputResponse;
import com.mobiquity.packer.service.ProcessPackageService;

/**
 * This is the controller class for the API
 * 
 * @author Nishchal
 *
 */
@RestController
public class PackerController {

	@Autowired
	private ProcessPackageService packageService;

	private static final Logger log = LoggerFactory.getLogger(PackerController.class);

	/**
	 * This is the controller method which send request to service layer and
	 * returns json Response
	 * 
	 * @param request
	 *            contains the path of the file
	 * @return API returns the indexes of items in each package
	 * @throws APIException
	 *             Custom exception
	 */
	@PostMapping(value = "/getPackage")
	public ResponseEntity<PackerOutputResponse> getPackage(@RequestBody PackerInputRequest request)
			throws APIException {
		log.info(request.getFilePath());
		PackerOutputResponse finalizedPackages = packageService.processPackage(request.getFilePath());
		return new ResponseEntity<>(finalizedPackages, HttpStatus.OK);
	}

}
