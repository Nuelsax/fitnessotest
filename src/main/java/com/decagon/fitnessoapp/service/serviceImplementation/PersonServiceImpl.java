package com.decagon.fitnessoapp.service.serviceImplementation;

import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.exceptions.PersonNotFoundException;
import com.decagon.fitnessoapp.model.dto.ChangePassword;
import com.decagon.fitnessoapp.model.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@Service
@RequiredArgsConstructor
@Component
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;


    @Override
    public void updateUserDetails(UpdatePersonDetails updatePersonDetails) {

        Person existingPerson = personRepository.findPersonByUserName(updatePersonDetails.getUserName())
                .orElseThrow(
                        () -> new PersonNotFoundException("Person Not Found")
                );

            existingPerson.setFirstName(updatePersonDetails.getFirstName());
            existingPerson.setLastName(updatePersonDetails.getLastName());
            existingPerson.setEmail(updatePersonDetails.getEmail());
            existingPerson.setGender(updatePersonDetails.getGender());
            existingPerson.setDateOfBirth(updatePersonDetails.getDateOfBirth());

            personRepository.save(existingPerson);
    }


    @Override
    public void updatePassword(ChangePassword changePassword) {
         Person currentPerson = personRepository.findPersonByPassword(changePassword.getCurrentPassword())
                 .orElseThrow(()-> new PersonNotFoundException("Person Not Found"));
         String newPassword = changePassword.getNewPassword();
         String confirmPassword = changePassword.getConfirmPassword();
         if (newPassword.equals(confirmPassword)){
             currentPerson.setPassword(newPassword);
             personRepository.save(currentPerson);
         }
    }



}
