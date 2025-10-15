package takeHomeAssignment.backend.demo.service;

import org.springframework.web.multipart.MultipartFile;

import takeHomeAssignment.backend.demo.dto.UploadResultDto;

public interface ProductService {

	public UploadResultDto parseAndSave(MultipartFile file);

}
