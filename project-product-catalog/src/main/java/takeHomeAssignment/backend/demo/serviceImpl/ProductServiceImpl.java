package takeHomeAssignment.backend.demo.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import takeHomeAssignment.backend.demo.dto.UploadResultDto;
import takeHomeAssignment.backend.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Override
	public UploadResultDto parseAndSave(MultipartFile file) {
		
		try {
			// ... parsing ...

			int storedCount = 0; // The actual number of valid rows saved
			List<Map<String, Object>> failureList = new java.util.ArrayList<>(); // The list of failed records

			// After processing and saving... and update storedCount and failureList

			// Return the final result
			return new UploadResultDto(storedCount, failureList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
