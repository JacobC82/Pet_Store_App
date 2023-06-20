package pet.store.service;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
//  is line above correct?
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao;
	@Autowired
	private EmployeeDao employeeDao;
	

	public PetStoreData savePetStoreData(PetStoreData petStoreData) {
		PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
		
		copyPetStoreFields(petStore, petStoreData);
		
		PetStore petStoreDAO = petStoreDao.save(petStore);
		
		return new PetStoreData(petStoreDAO);
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.petStoreId = (petStoreData.getPetStoreId());
		petStore.petStoreName = (petStoreData.getPetStoreName());
		petStore.petStoreAddress = (petStoreData.getPetStoreAddress());
		petStore.petStoreCity = (petStoreData.getPetStoreCity());
		petStore.petStoreState = (petStoreData.getPetStoreState());
		petStore.petStoreZip = (petStoreData.getPetStoreZip());
		petStore.petStorePhone = (petStoreData.getPetStorePhone());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		
		PetStore petStore;
		
		if(Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		}
		else {
			petStore = findPetStoreById(petStoreId);
				
		}
		
		return petStore;
	}
	//Red Squiggly @ findById, if I change to optional, 
	//red squiggly lines show on else block	

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow(()
				-> new NoSuchElementException("Pet store with ID = " 
						+ petStoreId + " is not a valid ID number."));
	}
	
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		Long employeeId = petStoreEmployee.getEmployeeId();
		findPetStoreById(petStoreId);
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		copyEmployeeFields(employee, petStoreEmployee);
		PetStore petStore = findPetStoreById(petStoreId);
		//petStore.getEmployees().add(employee);
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		return new PetStoreEmployee(employeeDao.save(employee));
		
	}

	
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		PetStore petStore = findPetStoreById(petStoreId); 
		Employee employee =  employeeDao.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Pet store with ID= "
				+ petStoreId + " does not have an employee with employee ID= " + employeeId + "."));
		
		if(petStore.getPetStoreId()!= petStoreId) {
			throw new IllegalArgumentException("No such employee ID for a Pet Store ID= " + petStoreId);
		}
		return employee;
	}
	
	private Employee findOrCreateEmployee(Long employeeId, Long petStoreId) {
	                      //Delete this if this doesnt work	
		Employee employee = findEmployeeById(employeeId,petStoreId);

		if(Objects.isNull(employeeId)) {
			Optional<Employee> opEmployee =
					employeeDao.findById(employeeId);
		if(opEmployee.isPresent()) {
			throw new DuplicateKeyException("Employee with ID = " + employeeId + " already exists");
		}
			
			employee = new Employee();
		}else {
			
			employee = findEmployeeById(petStoreId, employeeId);
		}
		return employee;
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}

	
}
