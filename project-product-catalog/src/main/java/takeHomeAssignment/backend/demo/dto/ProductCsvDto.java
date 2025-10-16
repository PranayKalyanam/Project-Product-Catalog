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
    @CsvBindByName(column = "sku", required = true)
    private String sku; 

	@NotEmpty(message = "Please enter valid name !")
    @CsvBindByName(column = "name", required = true)
    private String name;
    
	@NotEmpty(message = "Please enter valid brand !")
    @CsvBindByName(column = "brand", required = true)
    private String brand;
    
    @CsvBindByName(column = "color")
    private String color;
    
    @CsvBindByName(column = "size")
    private String size;
    
    @NotNull(message = "Please enter valid mrp !")
    @CsvBindByName(column = "mrp", required = true)
    private String mrp;
    
    @NotNull(message = "Please enter valid price !")
    @CsvBindByName(column = "price", required = true) 
    private String price;
    
    @Min(value = 1, message = "Quantity should be greater than 0")
    @CsvBindByName(column = "quantity")
    private String quantity;

}

