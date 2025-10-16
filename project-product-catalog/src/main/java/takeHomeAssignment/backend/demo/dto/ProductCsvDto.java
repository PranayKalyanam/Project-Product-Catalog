package takeHomeAssignment.backend.demo.dto;

import com.opencsv.bean.CsvBindByName;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCsvDto {

	@Column(unique = true)
    @CsvBindByName(column = "sku")
    private String sku; 

	@NotEmpty(message = "Please enter valid name !")
    @CsvBindByName(column = "name")
    private String name;
    
	@NotEmpty(message = "Please enter valid brand !")
    @CsvBindByName(column = "brand")
    private String brand;
    
    @CsvBindByName(column = "color")
    private String color;
    
    @CsvBindByName(column = "size")
    private String size;
    
    @NotNull(message = "Please enter valid mrp !")
    @CsvBindByName(column = "mrp")
    private String mrp;
    
    @NotNull(message = "Please enter valid price !")
    @CsvBindByName(column = "price") 
    private String price;
    
    @Min(value = 1, message = "Quantity should be greater than 0")
    @CsvBindByName(column = "quantity")
    private String quantity;

}

