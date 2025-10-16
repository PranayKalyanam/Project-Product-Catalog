package takeHomeAssignment.backend.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import takeHomeAssignment.backend.demo.dto.ProductCsvDto;
import takeHomeAssignment.backend.demo.serviceImpl.CsvServiceImpl;

public class CsvServiceParseTest {

	private final CsvServiceImpl csvService = new CsvServiceImpl();

	private static final String CSV_HEADERS = "sku,name,brand,color,size,mrp,price,quantity\n";

	@Test
	void testParseCsv_Success() throws IOException {
		// Valid CSV content with headers + two data rows
		String csvContent = CSV_HEADERS + "SKU001,Laptop,Dell,Black,15,1200,1000,5\n"
				+ "SKU002,Mouse,Logitech,Grey,Small,50,45,10";

		// Mock the MultipartFile upload
		MockMultipartFile file = new MockMultipartFile("file", "products.csv", "text/csv",
				csvContent.getBytes(StandardCharsets.UTF_8));

		// Action
		List<ProductCsvDto> results = csvService.parseCsv(file);

		// Assert
		assertNotNull(results);
		assertEquals(2, results.size());

		// Verify the first DTO was parsed correctly
		ProductCsvDto dto1 = results.get(0);
		assertEquals("SKU001", dto1.getSku());
		assertEquals("Laptop", dto1.getName());
		assertEquals("1000", dto1.getPrice(), "Price should be parsed as String '1000'");
	}

	@Test
	void testParseCsv_EmptyFile_ReturnsEmptyList() throws IOException {
		// only headers
		String emptyContent = CSV_HEADERS;

		MockMultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv",
				emptyContent.getBytes(StandardCharsets.UTF_8));

		// Act
		List<ProductCsvDto> results = csvService.parseCsv(file);

		// Assert
		assertTrue(results.isEmpty());
	}

	@Test
	void testParseCsv_MissingColumn_ThrowsIOException() throws IOException {
		// Missing a critical column like 'brand'
		String missingColumnContent = "sku,name,color,size,mrp,price,quantity\n" + "SKU003,Monitor,Black,L,500,450,2";

		MockMultipartFile file = new MockMultipartFile("file", "bad_headers.csv", "text/csv",
				missingColumnContent.getBytes(StandardCharsets.UTF_8));

		// Act & Assert
		assertThrows(IOException.class, () -> csvService.parseCsv(file),
				"Should throw IOException because the required 'brand' column is missing.");
	}

	@Test
	void testParseCsv_MalformedRow_ThrowsIOException() throws IOException {
		// Use a row that has fewer columns than the header defines (8 columns).
		String malformedContent = CSV_HEADERS +
		// This row has only 6 columns, which should cause a failure.
				"SKU004,Product A,BrandB,Black,10,200";

		MockMultipartFile file = new MockMultipartFile("file", "malformed.csv", "text/csv",
				malformedContent.getBytes(StandardCharsets.UTF_8));

		// Act & Assert
		// OpenCSV failure should be wrapped in an IOException by the service
		assertThrows(IOException.class, () -> csvService.parseCsv(file),
				"Should throw IOException for a row with missing fields.");
	}
}