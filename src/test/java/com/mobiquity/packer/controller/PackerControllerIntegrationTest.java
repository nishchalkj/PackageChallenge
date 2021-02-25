package com.mobiquity.packer.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mobiquity.packer.PackerApplication;
import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.PackerInputRequest;
import com.mobiquity.packer.model.PackerOutputResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PackerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PackerControllerIntegrationTest {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	@Test
	public void givenValidInputFilePath_WhenApiIsCalled_ThenReturnSuccessResponse() {

		HttpEntity<PackerInputRequest> entity = prepareDataForSuccessfulResponse();

		ResponseEntity<PackerOutputResponse> response = restTemplate.exchange(createURLWithPort("/getPackage"),
				HttpMethod.POST, entity, PackerOutputResponse.class);

		assertEquals("[4, -, 2,7, 8,9]", response.getBody().getIndexes().toString());
	}

	@Test
	public void givenWeightLessThanEveryItemInputFilePath_WhenApiIsCalled_ThenNoIndexesAreSelected()
			throws APIException {

		HttpEntity<PackerInputRequest> entity = prepareDataForNoIndexSelectedResponse();

		ResponseEntity<PackerOutputResponse> response = restTemplate.exchange(createURLWithPort("/getPackage"),
				HttpMethod.POST, entity, PackerOutputResponse.class);

		assertEquals("[-, -, -, -]", response.getBody().getIndexes().toString());

	}

	@Test
	public void givenEmptyInputFilePath_WhenApiIsCalled_ThenReturnEmptyResponse() {

		HttpEntity<PackerInputRequest> entity = prepareDataForEmptyResponse();

		ResponseEntity<PackerOutputResponse> response = restTemplate.exchange(createURLWithPort("/getPackage"),
				HttpMethod.POST, entity, PackerOutputResponse.class);

		assertEquals("[]", response.getBody().getIndexes().toString());

	}

	@Test
	public void givenInvalidInputFilePath_WhenApiIsCalled_ThenAPIExceptionIsThrown() {

		HttpEntity<PackerInputRequest> entity = prepareDataForAPIException();

		ResponseEntity<PackerOutputResponse> response = restTemplate.exchange(createURLWithPort("/getPackage"),
				HttpMethod.POST, entity, PackerOutputResponse.class);

		assertEquals(400, response.getStatusCodeValue());
	}

	private HttpEntity<PackerInputRequest> prepareDataForSuccessfulResponse() {

		PackerInputRequest request = new PackerInputRequest();
		request.setFilePath("src/test/resources/example_input");
		HttpEntity<PackerInputRequest> entity = new HttpEntity<>(request, headers);
		return entity;
	}

	private HttpEntity<PackerInputRequest> prepareDataForEmptyResponse() {

		PackerInputRequest request = new PackerInputRequest();
		request.setFilePath("src/test/resources/example_inputEmpty");
		HttpEntity<PackerInputRequest> entity = new HttpEntity<>(request, headers);
		return entity;
	}

	private HttpEntity<PackerInputRequest> prepareDataForAPIException() {

		PackerInputRequest request = new PackerInputRequest();
		request.setFilePath("src/test/resources/example_invalidPath");
		HttpEntity<PackerInputRequest> entity = new HttpEntity<>(request, headers);
		return entity;
	}

	private HttpEntity<PackerInputRequest> prepareDataForNoIndexSelectedResponse() {

		PackerInputRequest request = new PackerInputRequest();
		request.setFilePath("src/test/resources/example_inputwithweightlimitlessthaneveryitem");
		HttpEntity<PackerInputRequest> entity = new HttpEntity<>(request, headers);
		return entity;
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
