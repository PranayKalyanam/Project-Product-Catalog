package takeHomeAssignment.backend.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	@NotEmpty(message = "Please enter valid sku !")
	private String sku;
	
	@NotEmpty(message = "Please enter valid name !")
	private String name;
	
	@NotEmpty(message = "Please enter valid brand !")
	private String brand;
	
	private String color;
	
	private String size;
	
	@NotNull(message = "Please enter valid mrp !")
	private Long mrp;
	
	@NotNull(message = "Please enter valid price !")
	private Long price;
	
	@NotNull(message = "Please enter valid quantity !")
	@Min(value = 0, message = "Quantity should be greater than 0")
	private Integer quantity;

	public Boolean isValid() {

		if (null == sku || sku.isEmpty()) {
			return false;
		}
		if (null == name || name.isEmpty()) {
			return false;
		}
		if (null == brand || brand.isEmpty()) {
			return false;
		}

		if (null == mrp || mrp <= 0) {
			return false;
		}
		if (null == price || price <= 0) {
			return false;
		}
		if (quantity < 0) {
			return false;
		}

		if (price > mrp) {
			return false;
		}

		return true;

	}
}
