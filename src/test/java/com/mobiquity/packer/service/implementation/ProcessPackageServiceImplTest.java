package com.mobiquity.packer.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mobiquity.packer.exception.APIException;
import com.mobiquity.packer.model.PackerOutputResponse;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class ProcessPackageServiceImplTest {

	@InjectMocks
	private ProcessPackageServiceImpl service;

	@Spy
	private PackerOutputResponse output = new PackerOutputResponse();

	@Test
	public void givenValidInputFilePath_WhenApiIsCalled_ThenReturnSuccessResponse() throws APIException {

		PackerOutputResponse response = service.processPackage("src/test/resources/example_input");

		assertEquals("[4, -, 2,7, 8,9]", response.getIndexes().toString());

	}

	@Test
	public void givenWeightLessThanEveryItemInputFilePath_WhenApiIsCalled_ThenNoIndexesAreSelected()
			throws APIException {

		PackerOutputResponse response = service
				.processPackage("src/test/resources/example_inputwithweightlimitlessthaneveryitem");

		assertEquals("[-, -, -, -]", response.getIndexes().toString());
		

	}

	@Test
	public void givenEmptyInputFilePath_WhenApiIsCalled_ThenReturnEmptyResponse() throws APIException {

		PackerOutputResponse response = service.processPackage("src/test/resources/example_inputEmpty");

		assertEquals("[]", response.getIndexes().toString());

	}

	@Test(expected = APIException.class)
	public void givenInvalidInputFilePath_WhenApiIsCalled_ThenAPIExceptionIsThrown() throws APIException {

		PackerOutputResponse response = service.processPackage("src/test/resources/example_invalidPath");

	}

}
