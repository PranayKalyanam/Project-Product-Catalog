package takeHomeAssignment.backend.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import takeHomeAssignment.backend.demo.dto.ProcessingResult;
import takeHomeAssignment.backend.demo.dto.ProductCsvDto;

public interface CsvService {
	
	 public List<ProductCsvDto> parseCsv(MultipartFile file) throws IOException;

	 public ProcessingResult convertToEntities(List<ProductCsvDto> dtos);
}
