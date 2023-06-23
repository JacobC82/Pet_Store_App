package pet.store.service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
//  is line above correct?
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private CustomerDao customerDao;
	

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
		
		PetStore petStore = findPetStoreById(petStoreId);
		
		Long employeeId = petStoreEmployee.getEmployeeId();
		
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		
		copyEmployeeFields(employee, petStoreEmployee);
		
		employee.setPetStore(petStore);

		petStore.getEmployees().add(employee);
		
		Employee dbEmployee = employeeDao.save(employee);
		
		return new PetStoreEmployee(dbEmployee);
		
	}

	
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		PetStore petStore = findPetStoreById(petStoreId); 
		Employee employee =  employeeDao.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Pet store with ID= "
				+ petStoreId + " does not have an employee with employee ID= " + employeeId + "."));
		
//		if(petStore.getPetStoreId()!= petStoreId) {
//			
//		}
		
		
		if(petStoreId == employee.getPetStore().getPetStoreId()) {
			return employee;
		}else {
			throw new IllegalArgumentException("No such employee ID for a Pet Store ID= " + petStoreId);
		}
		
	}
	
	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
	                      //Delete this if this doesnt work	
		
		//Employee employee;

		if(Objects.isNull(employeeId)) {
		//	Optional<Employee> opEmployee =
//					employeeDao.findById(employeeId);
//		if(opEmployee.isPresent()) {
//			throw new DuplicateKeyException("Employee with ID = " + employeeId + " already exists");
		return new Employee();
		
		}
		
		return findEmployeeById(petStoreId, employeeId);
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}

	
	private Customer findCustomerById(Long petStoreId, Long customerId) {
		//500Here
		//PetStore petStore = findPetStoreById(petStoreId);
		
		Customer customer = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementException("l"));
				//("Pet store with ID= " + 
		 //petStoreId + " does not have a customer with a customer ID of " + customerId));
		
		//404Here
		//PetStore petStore = findPetStoreById(petStoreId);
		for(PetStore petStore : customer.getPetStore()) {
			if(petStore.getPetStoreId()== petStoreId) {
				return customer;
			}
		}throw new IllegalArgumentException("No such customer ID for pet store ID= " +petStoreId);
		
//		if(petStore.getPetStoreId() != petStoreId){
//			throw new IllegalArgumentException("No such customer ID for pet store ID= " +petStoreId);	
//		}
//		return customer;
					
	}	
	
	public Customer findOrCreateCustomer(Long customerId, Long petStoreId) {
		if (customerId == null){
			System.out.println("no customer found!");
			return new Customer();
		}
		
		return findCustomerById(petStoreId, customerId);
	}
	
	public void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		
		PetStore petStore = findPetStoreById(petStoreId);
		
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(customerId, petStoreId);
		
		copyCustomerFields(customer, petStoreCustomer);
		
		customer.getPetStore().add(petStore); //????
		
		petStore.getCustomers().add(customer);
		
		Customer dbCustomer = customerDao.save(customer);
		
		return new PetStoreCustomer(dbCustomer);
	}

//	public List<PetStoreData> retrieveAllPetStores() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
}
