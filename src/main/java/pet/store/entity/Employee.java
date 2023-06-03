package pet.store.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Employee {
	
	private Long employeeId;
	
	private Long petStoreId;
	
	private String employeeFirstName;
	
	private String employeeLastName;
	
	private Long phone;
	
	private String jobTitle;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pet_store_id")
	PetStore petStore = new PetStore();
}
