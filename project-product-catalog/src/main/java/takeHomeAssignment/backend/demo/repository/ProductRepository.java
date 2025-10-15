package takeHomeAssignment.backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import takeHomeAssignment.backend.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
