package takeHomeAssignment.backend.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import takeHomeAssignment.backend.demo.dto.ProcessingResult;
import takeHomeAssignment.backend.demo.dto.ProductCsvDto;
import takeHomeAssignment.backend.demo.serviceImpl.CsvServiceImpl;

public class CsvServiceConvertTest {

	private final CsvServiceImpl csvService = new CsvServiceImpl();

	// DTO creation methods
	private ProductCsvDto createBaseDto(String sku, String name, String mrp, String price, String quantity) {
		ProductCsvDto dto = new ProductCsvDto();
		dto.setSku(sku);
		dto.setName(name);
		dto.setBrand("BrandX");
		dto.setColor("Red");
		dto.setSize("L");
		dto.setMrp(mrp);
		dto.setPrice(price);
		dto.setQuantity(quantity);
		return dto;
	}

	// Test Cases
	@Test
	void testConvertToEntities_AllValid() {

		List<ProductCsvDto> dtos = Arrays.asList(createBaseDto("SKU1", "Item A", "100", "90", "10"),
				createBaseDto("SKU2", "Item B", "200", "150", "0"));

		// Act
		ProcessingResult result = csvService.convertToEntities(dtos);

		// Assert
		assertEquals(2, result.getValidProducts().size(), "Should have 2 valid products.");
		assertTrue(result.getFailureRecords().isEmpty(), "Should have no failure records.");
	}

	@Test
	void testConvertToEntities_Failure_PriceGreaterThanMrp() {
		// Price (110) > MRP (100) -> Should be invalid
		ProductCsvDto invalidDto = createBaseDto("FAIL1", "Overpriced", "100", "110", "1");

		List<ProductCsvDto> dtos = List.of(invalidDto);

		// Act
		ProcessingResult result = csvService.convertToEntities(dtos);

		// Assert
		assertEquals(0, result.getValidProducts().size(), "No products should be valid.");
		assertEquals(1, result.getFailureRecords().size(), "Should have 1 failure record.");
		assertEquals("FAIL1", result.getFailureRecords().get(0).get("sku"), "Failure record SKU mismatch.");
	}

	@Test
	void testConvertToEntities_Failure_EmptyName() {
		// Name is an empty string -> Should be invalid
		ProductCsvDto invalidDto = createBaseDto("FAIL2", "", "100", "90", "1");

		List<ProductCsvDto> dtos = List.of(invalidDto);

		// Act
		ProcessingResult result = csvService.convertToEntities(dtos);

		// Assert
		assertEquals(0, result.getValidProducts().size());
		assertEquals(1, result.getFailureRecords().size());
		assertEquals("FAIL2", result.getFailureRecords().get(0).get("sku"));
	}

	@Test
	void testConvertToEntities_QuantityIsNull_SetsToZeroAndValidates() {
		// Quantity is null (should be treated as 0 in Convert() and pass validation)
		ProductCsvDto dto = createBaseDto("SKU_ZERO_Q", "Zero Stock", "50", "40", null);

		List<ProductCsvDto> dtos = List.of(dto);

		// Act
		ProcessingResult result = csvService.convertToEntities(dtos);

		// Assert
		assertEquals(1, result.getValidProducts().size());
		assertEquals(0, result.getFailureRecords().size());

		// Verify the quantity was correctly set to 0 in the entity
		assertEquals(0, result.getValidProducts().get(0).getQuantity());
	}

	
}
