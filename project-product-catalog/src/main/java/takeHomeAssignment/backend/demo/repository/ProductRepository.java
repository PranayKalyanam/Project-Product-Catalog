package takeHomeAssignment.backend.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import takeHomeAssignment.backend.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	@Query("SELECT pr FROM Product pr WHERE "
	        + "(:name IS NULL OR pr.name = :name) AND "
	        + "(:brand IS NULL OR pr.brand = :brand) AND "
	        + "(:color IS NULL OR pr.color = :color) AND "
	        + "(:maxPrice IS NULL OR pr.price <= :maxPrice) AND "
	        + "(:minPrice IS NULL OR pr.price >= :minPrice)")
	Page<Product> findProductsByParam(Pageable pageable, 
	        @Param("name") String name, 
	        @Param("brand") String brand, 
	        @Param("color") String color, 
	        @Param("maxPrice") Long maxPrice,
	        @Param("minPrice") Long minPrice);

}
