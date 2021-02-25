package com.mobiquity.packer.exception;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mobiquity.packer.model.APIExceptionResponse;

/**
 * Global exception handler for the application
 * 
 * @author Nishchal
 *
 */
@ControllerAdvice
public class RestResponseAPIExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * This method handles the custom exception thrown in the application
	 * 
	 * @param ex
	 *            APIException- exception to be thrown
	 * @return ResponseEntity<APIExceptionResponse>
	 */
	@ExceptionHandler(APIException.class)
	public ResponseEntity<APIExceptionResponse> customExceptionHandler(APIException ex) {
		APIExceptionResponse errorResponse = new APIExceptionResponse();
		errorResponse.setDescription(ex.getMessage());
		errorResponse.setStatusMsg("FAILURE");
		errorResponse.setTimeStamp(LocalDateTime.now(ZoneId.of("Europe/Amsterdam")));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method handles the generic exception thrown in the application
	 * 
	 * @param ex
	 *            Exception - exception to be thrown
	 * @return ResponseEntity<APIExceptionResponse>
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<APIExceptionResponse> genericExceptionHandler(Exception ex) {
		APIExceptionResponse errorResponse = new APIExceptionResponse();
		errorResponse.setDescription(ex.getMessage());
		errorResponse.setStatusMsg("FAILURE");
		errorResponse.setTimeStamp(LocalDateTime.now(ZoneId.of("Europe/Amsterdam")));
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
