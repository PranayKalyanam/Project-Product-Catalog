package takeHomeAssignment.backend.demo.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import takeHomeAssignment.backend.demo.dto.ProcessingResult;
import takeHomeAssignment.backend.demo.dto.ProductCsvDto;
import takeHomeAssignment.backend.demo.dto.UploadResultDto;
import takeHomeAssignment.backend.demo.entity.Product;
import takeHomeAssignment.backend.demo.repository.ProductRepository;
import takeHomeAssignment.backend.demo.service.CsvService;
import takeHomeAssignment.backend.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private CsvService csvService;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public UploadResultDto parseAndSave(MultipartFile file) throws IOException {

		try {
			List<ProductCsvDto> listOfProducts = csvService.parseCsv(file);
			ProcessingResult processingResult = csvService.convertToEntities(listOfProducts);
			
			List<Product> validProducts = processingResult.getValidProducts();
			productRepository.saveAll(validProducts);
			int storedCount = 0;
			storedCount = validProducts.size();
			
			
			List<Map<String, Object>> failureList = new java.util.ArrayList<>();
			failureList = processingResult.getFailureRecords();
			
			

			return new UploadResultDto(storedCount, failureList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Failed to parse CSV file using OpenCSV: " + e.getMessage(), e);
		}
	}

}
