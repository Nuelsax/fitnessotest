package com.decagon.fitnessoapp.security;

import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersonDetailsService implements UserDetailsService {


    private PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUserName(userName);

        person.orElseThrow(()-> new UsernameNotFoundException("Not Found: " + userName));

        return person.map(PersonDetails::new).get();
    }


    public void updateResetPasswordToken(String token, String email) throws PersonNotFoundException{
        Person person = personRepository.findByEmail(email).get();
        if (person != null){
            person.setResetPasswordToken(token);
            personRepository.save(person);
        } else{
            throw new PersonNotFoundException("Could not find any user with the email " + email);
        }
    }

    public Person getByResetPasswordToken(String token){
        return personRepository.findByResetPasswordToken(token).get();
    }

    public void updatePassword(Person person, String newPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        person.setPassword(encodedPassword);

        person.setResetPasswordToken(null);
        personRepository.save(person);
    }

    public Person savePerson(Person person){
        return personRepository.save(person);
    }
}



