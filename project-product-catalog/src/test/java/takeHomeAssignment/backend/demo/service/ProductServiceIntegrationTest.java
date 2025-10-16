package takeHomeAssignment.backend.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import takeHomeAssignment.backend.demo.dto.ProcessingResult;
import takeHomeAssignment.backend.demo.dto.ProductCsvDto;
import takeHomeAssignment.backend.demo.dto.UploadResultDto;
import takeHomeAssignment.backend.demo.entity.Product;
import takeHomeAssignment.backend.demo.repository.ProductRepository;
import takeHomeAssignment.backend.demo.serviceImpl.ProductServiceImpl;

@SpringBootTest
public class ProductServiceIntegrationTest {

	// Inject the service implementation we are testing
	@Autowired
	private ProductServiceImpl productService;

	// Mock the external service it depends on
	@SuppressWarnings("removal")
	@MockBean
	private CsvService csvService;

	// Mock the database dependency
	@SuppressWarnings("removal")
	@MockBean
	private ProductRepository productRepository;

	private final MockMultipartFile mockFile = new MockMultipartFile("file", "test.csv", "text/csv",
			"some,data".getBytes(StandardCharsets.UTF_8));

	private Product createProduct(String sku) {
		Product p = new Product();
		p.setSku(sku);
		return p; // Minimal valid product
	}

	private Map<String, Object> createFailureRecord(String sku) {
		return Map.of("sku", sku, "reason", "Test Failure");
	}

	//Test Cases
	@Test
	void testParseAndSave_AllValid_Success() throws IOException {
		List<Product> validList = List.of(createProduct("SKU001"), createProduct("SKU002"));
		ProcessingResult mockResult = new ProcessingResult(validList, Collections.emptyList());

		// Mock the CsvService's behavior: it will return DTOs, then convert them to our
		// mock result.
		when(csvService.parseCsv(any(MockMultipartFile.class)))
				.thenReturn(List.of(new ProductCsvDto(), new ProductCsvDto()));
		when(csvService.convertToEntities(anyList())).thenReturn(mockResult);

		// Act
		UploadResultDto resultDto = productService.parseAndSave(mockFile);

		// Assert
		// 1. Verify the final DTO returned to the controller/user
		assertEquals(2, resultDto.stored(), "Should report 2 successfully stored products.");
		assertTrue(resultDto.failed().isEmpty(), "Should report 0 failures.");

		// 2. Verify Repository Interaction (Saved the valid products)
		verify(productRepository, times(1)).saveAll(validList);
	}


	@Test
	void testParseAndSave_MixedData_PartialSuccess() throws IOException {
		// Arrange
		List<Product> validList = List.of(createProduct("SKU003"));
		List<Map<String, Object>> failureList = List.of(createFailureRecord("SKU004"));
		ProcessingResult mockResult = new ProcessingResult(validList, failureList);

		// Mock the CsvService to simulate a mixed outcome
		when(csvService.parseCsv(any(MockMultipartFile.class)))
				.thenReturn(List.of(new ProductCsvDto(), new ProductCsvDto()));
		when(csvService.convertToEntities(anyList())).thenReturn(mockResult);

		// Act
		UploadResultDto resultDto = productService.parseAndSave(mockFile);

		// Assert
		// 1. Verify the final DTO returned
		assertEquals(1, resultDto.stored(), "Should report 1 stored product.");
		assertEquals(1, resultDto.failed().size(), "Should report 1 failure.");
		assertEquals("SKU004", resultDto.failed().get(0).get("sku"), "Failure SKU should be reported.");

		// 2. Verify Repository Interaction (Saved only the valid product)
		verify(productRepository, times(1)).saveAll(validList);
	}


	@Test
	void testParseAndSave_ParsingFailure_ThrowsIOExceptionAndSkipsSave() throws IOException {

		// Mock the CsvService's parseCsv to throw an IOException
		when(csvService.parseCsv(any(MockMultipartFile.class)))
				.thenThrow(new IOException("Simulated CSV parsing error"));

		// Act & Assert
		// 1. The service implementation should catch the Exception
		assertThrows(IOException.class, () -> productService.parseAndSave(mockFile),
				"Should re-throw IOException when initial parsing fails.");

		// 2. Verify Repository Interaction: Save should NEVER be called
		verify(productRepository, never()).saveAll(anyList());

		// 3. Verify CsvService.convertToEntities: Should NEVER be called
		verify(csvService, never()).convertToEntities(anyList());
	}
}