package takeHomeAssignment.backend.demo.restController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import takeHomeAssignment.backend.demo.dto.UploadResultDto;
import takeHomeAssignment.backend.demo.service.ProductService;

@RestController
public class ProductRestController {

	@Autowired
	private ProductService productService;

	@GetMapping("/hello")
	public ResponseEntity<String> getInfo() {

		return ResponseEntity.ok("hello world!!!!!!!!!!!!");

		
		
		
	}

	@PostMapping("/upload")
	public ResponseEntity<UploadResultDto> postCSVFile(@RequestParam(required = true) MultipartFile file) throws IOException{

		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(new UploadResultDto(0, List.of(Map.of("error", "File is empty!"))));
		}
		
		UploadResultDto result = productService.parseAndSave(file);
		return ResponseEntity.ok(result);

	}

}
