package takeHomeAssignment.backend.demo.restController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import takeHomeAssignment.backend.demo.dto.UploadResultDto;
import takeHomeAssignment.backend.demo.entity.Product;
import takeHomeAssignment.backend.demo.service.ProductService;
import takeHomeAssignment.backend.demo.util.PaginationParam;

@RestController
public class ProductRestController {

	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	public ResponseEntity<String> hello() {
		
		return ResponseEntity.ok("hello");		
	}

	@PostMapping("/upload")
	public ResponseEntity<UploadResultDto> postCSVFile(@RequestParam(required = true) MultipartFile file) throws IOException{

		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(new UploadResultDto(0, List.of(Map.of("error", "File is empty!"))));
		}
		
		UploadResultDto result = productService.parseAndSave(file);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/products")
	public ResponseEntity<Page<Product>> getAllProducts() {
		PaginationParam paginationParam = new PaginationParam();
		Page<Product> result = productService.findAllProducts(paginationParam);
		return ResponseEntity.ok(result);		
	}
	
	@GetMapping("/products/search")
	public ResponseEntity<Page<Product>> searchProducts(@RequestParam(required = false) String name, @RequestParam(required = false) String brand, @RequestParam(required = false) String color, @RequestParam(required = false) Long maxPrice, @RequestParam(required = false) Long minPrice) {
		PaginationParam paginationParam = new PaginationParam();
		Page<Product> result = productService.findProductsByParam(paginationParam, name, brand, color, maxPrice, minPrice);
		return ResponseEntity.ok(result);
	}

}
