package pet.store.service;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//  is line above correct?
import org.springframework.stereotype.Service;

import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao;
	

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
}
