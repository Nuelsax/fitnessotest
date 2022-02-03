package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.FavouriteResponse;
import com.decagon.fitnessoapp.model.user.Favourite;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.FavouriteRepository;
import com.decagon.fitnessoapp.repository.IntangibleProductRepository;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.repository.TangibleProductRepository;
import com.decagon.fitnessoapp.service.FavouriteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavouriteServiceImpl implements FavouriteService {

    private PersonRepository personRepository;
    private TangibleProductRepository tangibleProductRepository;
    private IntangibleProductRepository intangibleProductRepository;
    private FavouriteRepository favouriteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public FavouriteServiceImpl(PersonRepository personRepository, FavouriteRepository favouriteRepository, IntangibleProductRepository intangibleProductRepository, TangibleProductRepository tangibleProductRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.favouriteRepository = favouriteRepository;
        this.intangibleProductRepository = intangibleProductRepository;
        this.tangibleProductRepository = tangibleProductRepository;
        this.modelMapper = modelMapper;
    }




    @Override
    public ResponseEntity<String> addOrDeleteFavourite(Long productId, Authentication authentication) {

        Person person = personRepository.findPersonByUserName(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found in favourite Service Implementation"));

//        FavouriteResponse favouriteResponse = new FavouriteResponse();
       Favourite favourite = new Favourite();
        Optional<Favourite> itemToDelete = favouriteRepository.findFavouriteByProductId(productId);
        if(itemToDelete.isPresent()){
            favouriteRepository.deleteFavouriteByProductId(productId);
            return ResponseEntity.ok().body("Item successfully deleted from favourite");
        }
        else{
            favourite.setId(productId);
            favourite.setPerson(person);
            favouriteRepository.save(favourite);
        }


        return ResponseEntity.ok().body("Item successfully added to favourite");
    }

    @Override
    public FavouriteResponse viewFavourites(Person person, Authentication authentication) {
       FavouriteResponse favouriteResponse = new FavouriteResponse();
        Person person1 = personRepository.findPersonByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found in favourite Service Implementation"));

        List<Favourite> favouriteResponseList = favouriteRepository.findFavouritesByPersonId(person1.getId());
//        FavouriteResponse favouriteResponse = new FavouriteResponse();
        Favourite favourite = new Favourite();
        favourite.setPerson(person1);
        for (Favourite perFavourite : favouriteResponseList) {
//            modelMapper.map(tangibleProductRepository.findById(perFavourite.getProductId()), );
//            Optional<IntangibleProduct> intangibleProduct = intangibleProductRepository.findById(perFavourite.getProductId());
//            if(tangibleProduct.isPresent())
//
//
//            if(tangibleProduct == null){
//
//            }
//
//
//
//            return perFavouriteResponse;
        }

return favouriteResponse;
}
}
