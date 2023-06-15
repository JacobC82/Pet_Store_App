package pet.store.controller.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	
	@Autowired
	private PetStoreService petStoreService;	
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStoreData(@RequestBody PetStoreData petStoreData) {
		
		log.info("Creating pet store data {}", petStoreData);
		
		return petStoreService.savePetStoreData(petStoreData);
		
	}
	
	@PutMapping("/pet_store/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId,
									@RequestBody PetStoreData petStoreData) {
		
		petStoreData.setPetStoreId(petStoreId);
		
		log.info("Updating Pet Store {}", petStoreData);
		
		return petStoreService.savePetStoreData(petStoreData);
		
	}
}
