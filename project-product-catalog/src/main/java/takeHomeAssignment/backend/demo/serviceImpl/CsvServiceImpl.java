package takeHomeAssignment.backend.demo.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBeanBuilder;

import takeHomeAssignment.backend.demo.dto.ProcessingResult;
import takeHomeAssignment.backend.demo.dto.ProductCsvDto;
import takeHomeAssignment.backend.demo.entity.Product;
import takeHomeAssignment.backend.demo.service.CsvService;

@Service
public class CsvServiceImpl implements CsvService {

	@Override
	public List<ProductCsvDto> parseCsv(MultipartFile file) throws IOException {
		try {
			Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

			List<ProductCsvDto> listOfProducts = new CsvToBeanBuilder<ProductCsvDto>(reader)
					.withType(ProductCsvDto.class).withIgnoreLeadingWhiteSpace(true).build().parse();

			return listOfProducts;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Failed to parse CSV file using OpenCSV: " + e.getMessage(), e);
		}
	}

	@Override
	public ProcessingResult convertToEntities(List<ProductCsvDto> dtos) {
		List<Product> allProducts = dtos.stream().map(this::Convert).toList();

		List<Product> validProducts = allProducts.stream().filter(r -> r.isValid()).collect(Collectors.toList());

		List<Map<String, Object>> failureRecords = allProducts.stream().filter(r -> !r.isValid())
				.map(r -> {
	                Map<String, Object> map = new HashMap<>();
	                map.put("sku", r.getSku());
	                map.put("name", r.getName());
	                map.put("brand", r.getBrand());
	                map.put("color", r.getColor());
	                map.put("size", r.getSize());
	                map.put("mrp_value", r.getMrp());
	                map.put("price_value", r.getPrice());
	                map.put("quantity_value", r.getQuantity());
	                return map;
	            })
				.collect(Collectors.toList());
		
		ProcessingResult processingResult = new ProcessingResult();
		processingResult.setValidProducts(validProducts);
		processingResult.setFailureRecords(failureRecords);
		
		
		return processingResult;
	}

	private Product Convert(ProductCsvDto dto) {
		Product product = new Product();
		product.setSku(dto.getSku());
		product.setName(dto.getName());
		product.setBrand(dto.getBrand());
		product.setColor(dto.getColor());
		product.setSize(dto.getSize());
		product.setMrp(Long.parseLong(dto.getMrp()));
		product.setPrice(Long.parseLong(dto.getPrice()));
		if (null == dto.getQuantity()) {
			product.setQuantity(0);
		} else {
			product.setQuantity(Integer.parseInt(dto.getQuantity()));
		}

		return product;

	}

//	private Boolean isValid(Product product) {
//
//		if (product.getSku().isEmpty()) {
//			return false;
//		}
//		if (product.getName().isEmpty()) {
//			return false;
//		}
//		if (product.getBrand().isEmpty()) {
//			return false;
//		}
//
//		if (null == product.getMrp()) {
//			return false;
//		}
//		if (null == product.getPrice()) {
//			return false;
//		}
//		if (product.getQuantity() < 0) {
//			return false;
//		}
//
//		if (product.getPrice() > product.getMrp()) {
//			return false;
//		}
//
//		return true;
//
//	}
//	
}
