package takeHomeAssignment.backend.demo.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import takeHomeAssignment.backend.demo.dto.UploadResultDto;
import takeHomeAssignment.backend.demo.entity.Product;
import takeHomeAssignment.backend.demo.util.PaginationParam;

public interface ProductService {

	public UploadResultDto parseAndSave(MultipartFile file) throws IOException;

	public Page<Product> findAllProducts(PaginationParam paginationParam);

	public Page<Product> findProductsByParam(PaginationParam paginationParam, String name, String brand, String color,
			Long maxPrice, Long minPrice);

}
