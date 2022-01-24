package com.decagon.fitnessoapp.security;

import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.exception.CustomServiceExceptions;
import com.decagon.fitnessoapp.model.dto.ChangePassword;
import com.decagon.fitnessoapp.model.dto.UpdatePersonDetails;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.service.PersonService;
import com.decagon.fitnessoapp.service.serviceImplementation.EmailSender;
import com.decagon.fitnessoapp.service.serviceImplementation.EmailValidator;
import com.decagon.fitnessoapp.service.serviceImplementation.VerificationTokenServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersonDetailsService implements UserDetailsService, PersonService{
    private final VerificationTokenServiceImpl verificationTokenService;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final PersonRepository personRepository;
    private final EmailValidator emailValidator;
    private final ModelMapper modelMapper;
    private final EmailSender emailSender;
    @Value("${website.address}")
    private String website;

    @Value("${server.port}")
    private int port;

    @Autowired
    public PersonDetailsService(VerificationTokenServiceImpl verificationTokenService, PasswordEncoder bCryptPasswordEncoder, PersonRepository personRepository, EmailValidator emailValidator, ModelMapper modelMapper, Environment env, EmailSender emailSender) {
        this.verificationTokenService = verificationTokenService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.personRepository = personRepository;
        this.emailValidator = emailValidator;
        this.modelMapper = modelMapper;
        this.emailSender = emailSender;
    }



    @Override
    public PersonDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUserName(userName);

        person.orElseThrow(()-> new UsernameNotFoundException("Not Found: " + userName));

        return person.map(PersonDetails::new).get();
    }



    public Person register(PersonDto personDto) {

        boolean isValidEmail = emailValidator.test(personDto.getEmail());
        if(!isValidEmail){
            throw new CustomServiceExceptions("Not valid email");
        }

        boolean userExists = personRepository.findByEmail(personDto.getEmail()).isPresent();
        if(userExists){
            throw  new CustomServiceExceptions("email taken");
        }

        Person person = new Person();
        modelMapper.map(personDto, person);

        final String encodedPassword = bCryptPasswordEncoder.encode(personDto.getPassword());
        person.setPassword(encodedPassword);
        personRepository.save(person);
        sendingEmail(personDto);
        return person;
    }

    public void sendingEmail(PersonDto personDto){
        Person person = personRepository.findByEmail(personDto.getEmail())
                .orElseThrow(() -> new CustomServiceExceptions("Email not registered"));
        String token = verificationTokenService.saveVerificationToken(person);
        String link = "http://"+ website + ":" + port + "/person/confirm?token=" + token;
        String subject = "Confirm your email";
        emailSender.send(subject, person.getEmail(), buildEmail(person.getFirstName(), link));
    }

    public void updateResetPasswordToken(String token, String email) throws CustomServiceExceptions{
        Person person = personRepository.findByEmail(email).get();
        if (person != null){
            person.setResetPasswordToken(token);
            personRepository.save(person);
        } else{
            throw new CustomServiceExceptions("Could not find any user with the email " + email);
        }

        String resetPasswordLink = "http://"+ website + ":" + port + "/reset_password?token=" + token;
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                + "<br>"
                + "<p> Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        emailSender.send(subject, person.getEmail(), resetPasswordLink);
    }

    public Person getByResetPasswordToken(String token){
        return personRepository.findByResetPasswordToken(token).get();
    }

    public void updatePassword(Person person, String newPassword){
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        person.setPassword(encodedPassword);

        person.setResetPasswordToken(null);
        personRepository.save(person);
    }


    public String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 24 hours. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
