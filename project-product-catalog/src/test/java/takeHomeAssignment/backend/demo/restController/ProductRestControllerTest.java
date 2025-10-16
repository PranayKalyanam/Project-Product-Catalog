package takeHomeAssignment.backend.demo.restController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import takeHomeAssignment.backend.demo.dto.UploadResultDto;
import takeHomeAssignment.backend.demo.service.ProductService;

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	// Mock the service layer dependency
	@SuppressWarnings("removal")
	@MockBean
	private ProductService productService;

	private final MockMultipartFile validMockFile = new MockMultipartFile("file", "products.csv", "text/csv",
			"sku,name\nSKU001,Shirt".getBytes());

	private final MockMultipartFile emptyMockFile = new MockMultipartFile("file", "empty.csv", "text/csv", new byte[0]);

	@Test
	void testPostCSVFile_Success_AllValid() throws Exception {
		// Service returns a successful result with no failures.
		UploadResultDto mockResult = new UploadResultDto(10, Collections.emptyList());
		when(productService.parseAndSave(any())).thenReturn(mockResult);

		// Act & Assert
		mockMvc.perform(multipart("/upload").file(validMockFile)).andExpect(status().isOk()) // Expect HTTP 200
				.andExpect(jsonPath("$.stored").value(10)).andExpect(jsonPath("$.failed").isEmpty());
	}

	@Test

	void testPostCSVFile_Success_WithFailures() throws Exception {
		// Service returns a result with stored items and failures.
		List<Map<String, Object>> failures = List.of(Map.of("sku", "BAD_SKU", "reason", "Price too high"));
		UploadResultDto mockResult = new UploadResultDto(5, failures);
		when(productService.parseAndSave(any())).thenReturn(mockResult);

		// Act & Assert
		mockMvc.perform(multipart("/upload").file(validMockFile)).andExpect(status().isOk()) // HTTP 200 means the
																								// request was processed
																								// successfully
				.andExpect(jsonPath("$.stored").value(5)).andExpect(jsonPath("$.failed[0].sku").value("BAD_SKU"))
				.andExpect(jsonPath("$.failed.size()").value(1));
	}

	@Test
	void testPostCSVFile_EmptyFile() throws Exception {
		// The controller explicitly checks for an empty file.
		// The controller returns: new UploadResultDto(0, List.of(Map.of("error", "File
		// is empty!")))

		// Act & Assert
		mockMvc.perform(multipart("/upload").file(emptyMockFile)).andExpect(status().isBadRequest()) // Expect HTTP 400
																										// if
																										// file.isEmpty().
				.andExpect(jsonPath("$.stored").value(0))
				.andExpect(jsonPath("$.failed[0].error").value("File is empty!"));
	}

	@Test
	void testPostCSVFile_ServiceThrowsIOException() throws Exception {
	    //  Mock the service to throw a RuntimeException instead, 
	    // which MockMvc handles more gracefully in a test context to return 500.
	    when(productService.parseAndSave(any())).thenThrow(new IOException("Simulated IO Exception"));

	    // Act & Assert
	    mockMvc.perform(multipart("/upload")
	            .file(validMockFile))
	            .andExpect(status().isInternalServerError()); // Expect HTTP 500
	}
}
