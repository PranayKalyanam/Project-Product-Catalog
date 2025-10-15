package takeHomeAssignment.backend.demo.dto;

import java.util.List;
import java.util.Map;

public record UploadResultDto(Integer stored, List<Map<String, Object>> failed) {

}
