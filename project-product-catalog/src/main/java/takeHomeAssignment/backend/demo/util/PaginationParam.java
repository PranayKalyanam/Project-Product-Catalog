package takeHomeAssignment.backend.demo.util;

import lombok.Data;

@Data
public class PaginationParam {
	private Integer page = 0;
	private Integer size = 10;
	private String sortBy = "id";
	private String sortDirection = "desc";

}
