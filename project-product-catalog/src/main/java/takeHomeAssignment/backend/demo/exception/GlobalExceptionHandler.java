package takeHomeAssignment.backend.demo.exception;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IOException.class)
	public ResponseEntity<Map<String, String>> handleIOException(IOException ex) {
		// Log the exception if needed
		System.err.println("Handling IOException: " + ex.getMessage());

		// Return a generic 500 response
		return new ResponseEntity<>(Map.of("error", "File processing failed: " + ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
