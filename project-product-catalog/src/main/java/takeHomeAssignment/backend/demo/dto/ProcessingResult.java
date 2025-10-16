package takeHomeAssignment.backend.demo.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import takeHomeAssignment.backend.demo.entity.Product;

@Data
@NoArgsConstructor
public class ProcessingResult {
	
	List<Product> validProducts; 
	List<Map<String, Object>> failureRecords;

}
