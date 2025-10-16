package takeHomeAssignment.backend.demo.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import takeHomeAssignment.backend.demo.dto.ProcessingResult;
import takeHomeAssignment.backend.demo.dto.ProductCsvDto;
import takeHomeAssignment.backend.demo.dto.UploadResultDto;
import takeHomeAssignment.backend.demo.entity.Product;
import takeHomeAssignment.backend.demo.repository.ProductRepository;
import takeHomeAssignment.backend.demo.service.CsvService;
import takeHomeAssignment.backend.demo.service.ProductService;
import takeHomeAssignment.backend.demo.util.PaginationParam;

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

	@Override
	public Page<Product> findAllProducts(PaginationParam paginationParam) {
		Pageable pageable = PageRequest.of(paginationParam.getPage(), paginationParam.getSize(),
				paginationParam.getSortDirection().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(paginationParam.getSortBy()).ascending()
						: Sort.by(paginationParam.getSortBy()).descending());
		
		
		Page<Product> pageOfProducts = productRepository.findAll(pageable);
		
		return pageOfProducts;
	}

	@Override
	public Page<Product> findProductsByParam(PaginationParam paginationParam, String name, String brand, String color,
			Long maxPrice, Long minPrice) {
		
		Pageable pageable = PageRequest.of(paginationParam.getPage(), paginationParam.getSize(),
				paginationParam.getSortDirection().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(paginationParam.getSortBy()).ascending()
						: Sort.by(paginationParam.getSortBy()).descending());
		
		
		Page<Product> pageOfProducts = productRepository.findProductsByParam(pageable, name, brand, color, maxPrice, minPrice);
		
		return pageOfProducts;
		
	}

}
