package com.decagon.fitnessoapp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.decagon.fitnessoapp.dto.PersonDto;
import com.decagon.fitnessoapp.model.user.Person;
import com.decagon.fitnessoapp.model.user.Role;
import com.decagon.fitnessoapp.repository.PersonRepository;
import com.decagon.fitnessoapp.service.serviceImplementation.EmailSender;
import com.decagon.fitnessoapp.service.serviceImplementation.EmailValidator;
import com.decagon.fitnessoapp.service.serviceImplementation.VerificationTokenServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PersonDetailsService.class})
@ExtendWith(SpringExtension.class)
class PersonDetailsServiceTest {
    @MockBean
    private EmailSender emailSender;

    @MockBean
    private EmailValidator emailValidator;

    @MockBean
    private Environment environment;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonDetailsService personDetailsService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private VerificationTokenServiceImpl verificationTokenServiceImpl;

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);
        when(this.personRepository.findByUserName((String) any())).thenReturn(ofResult);
        UserDetails actualLoadUserByUsernameResult = this.personDetailsService.loadUserByUsername("janedoe");
        Collection<? extends GrantedAuthority> authorities = actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertEquals("ANONYMOUS", ((List<? extends GrantedAuthority>) authorities).get(0).getAuthority());
        verify(this.personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(this.personRepository.findByUserName((String) any())).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> this.personDetailsService.loadUserByUsername("janedoe"));
        verify(this.personRepository).findByUserName((String) any());
    }

    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(this.personRepository.findByUserName((String) any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> this.personDetailsService.loadUserByUsername("janedoe"));
        verify(this.personRepository).findByUserName((String) any());
    }

    @Test
    void testRegister() {
        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);
        when(this.personRepository.findByEmail((String) any())).thenReturn(ofResult);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(IllegalStateException.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository).findByEmail((String) any());
    }

    @Test
    void testRegister2() {
        when(this.personRepository.findByEmail((String) any())).thenThrow(new IllegalStateException("foo"));

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(IllegalStateException.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository).findByEmail((String) any());
    }

    @Test
    void testRegister3() {
        when(this.personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        doNothing().when(this.modelMapper).map((Object) any(), (Object) any());
        when(this.emailValidator.test((String) any())).thenReturn(true);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(IllegalArgumentException.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository, atLeast(1)).findByEmail((String) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
        verify(this.modelMapper).map((Object) any(), (Object) any());
        verify(this.emailValidator).test((String) any());
    }

    @Test
    void testRegister4() {
        when(this.personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode((CharSequence) any())).thenThrow(new IllegalStateException("foo"));
        doNothing().when(this.modelMapper).map((Object) any(), (Object) any());
        when(this.emailValidator.test((String) any())).thenReturn(true);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(IllegalStateException.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository).findByEmail((String) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
        verify(this.modelMapper).map((Object) any(), (Object) any());
        verify(this.emailValidator).test((String) any());
    }

    @Test
    void testRegister5() {
        when(this.personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        doNothing().when(this.modelMapper).map((Object) any(), (Object) any());
        when(this.emailValidator.test((String) any())).thenReturn(false);

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(IllegalStateException.class, () -> this.personDetailsService.register(personDto));
        verify(this.personRepository).findByEmail((String) any());
        verify(this.emailValidator).test((String) any());
    }

    @Test
    void testSendingEmail() {
        when(this.verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");

        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);
        when(this.personRepository.findByEmail((String) any())).thenReturn(ofResult);
        when(this.environment.getProperty((String) any())).thenReturn("Property");
        doNothing().when(this.emailSender).send((String) any(), (String) any());

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        this.personDetailsService.sendingEmail(personDto);
        verify(this.verificationTokenServiceImpl).saveVerificationToken((Person) any());
        verify(this.personRepository).findByEmail((String) any());
        verify(this.environment, atLeast(1)).getProperty((String) any());
        verify(this.emailSender).send((String) any(), (String) any());
    }

    @Test
    void testSendingEmail2() {
        when(this.verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");

        Person person = new Person();
        person.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        person.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        person.setEmail("jane.doe@example.org");
        person.setFirstName("Jane");
        person.setGender("Gender");
        person.setId(123L);
        person.setImage("Image");
        person.setLastName("Doe");
        person.setPassword("iloveyou");
        person.setPhoneNumber("4105551212");
        person.setRole(Role.ANONYMOUS);
        person.setUserName("janedoe");
        person.setVerifyEmail(true);
        Optional<Person> ofResult = Optional.of(person);
        when(this.personRepository.findByEmail((String) any())).thenReturn(ofResult);
        when(this.environment.getProperty((String) any())).thenReturn("Property");
        doThrow(new IllegalStateException("website.address")).when(this.emailSender).send((String) any(), (String) any());

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(IllegalStateException.class, () -> this.personDetailsService.sendingEmail(personDto));
        verify(this.verificationTokenServiceImpl).saveVerificationToken((Person) any());
        verify(this.personRepository).findByEmail((String) any());
        verify(this.environment, atLeast(1)).getProperty((String) any());
        verify(this.emailSender).send((String) any(), (String) any());
    }

    @Test
    void testSendingEmail3() {
        when(this.verificationTokenServiceImpl.saveVerificationToken((Person) any())).thenReturn("ABC123");
        when(this.personRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(this.environment.getProperty((String) any())).thenReturn("Property");
        doNothing().when(this.emailSender).send((String) any(), (String) any());

        PersonDto personDto = new PersonDto();
        personDto.setAddress(new ArrayList<>());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        personDto.setDateOfBirth(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        personDto.setEmail("jane.doe@example.org");
        personDto.setFirstName("Jane");
        personDto.setGender("Gender");
        personDto.setImage("Image");
        personDto.setLastName("Doe");
        personDto.setPassword("iloveyou");
        personDto.setPhoneNumber("4105551212");
        personDto.setRole(Role.ANONYMOUS);
        personDto.setUserName("janedoe");
        personDto.setVerifyEmail(true);
        assertThrows(IllegalArgumentException.class, () -> this.personDetailsService.sendingEmail(personDto));
        verify(this.personRepository).findByEmail((String) any());
    }

    @Test
    void testBuildEmail() {
        assertEquals("<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" + "\n"
                        + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" + "\n"
                        + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%"
                        + "!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" + "    <tbody><tr>\n"
                        + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" + "        \n"
                        + "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\""
                        + " cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" + "          <tbody><tr>\n"
                        + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
                        + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border"
                        + "-collapse:collapse\">\n" + "                  <tbody><tr>\n"
                        + "                    <td style=\"padding-left:10px\">\n" + "                  \n"
                        + "                    </td>\n"
                        + "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">"
                        + "\n"
                        + "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff"
                        + ";text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
                        + "                    </td>\n" + "                  </tr>\n" + "                </tbody></table>\n"
                        + "              </a>\n" + "            </td>\n" + "          </tr>\n" + "        </tbody></table>\n"
                        + "        \n" + "      </td>\n" + "    </tr>\n" + "  </tbody></table>\n"
                        + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\""
                        + " cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\""
                        + " width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
                        + "      <td>\n" + "        \n"
                        + "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\""
                        + " style=\"border-collapse:collapse\">\n" + "                  <tbody><tr>\n"
                        + "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
                        + "                  </tr>\n" + "                </tbody></table>\n" + "        \n" + "      </td>\n"
                        + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" + "    </tr>\n" + "  </tbody></table>\n"
                        + "\n" + "\n" + "\n"
                        + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\""
                        + " cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\""
                        + " width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
                        + "    <tr>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
                        + "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max"
                        + "-width:560px\">\n" + "        \n"
                        + "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi Name,</p><p"
                        + " style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering."
                        + " Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px"
                        + " 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p"
                        + " style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"Link\">Activate"
                        + " Now</a> </p></blockquote>\n" + " Link will expire in 24 hours. <p>See you soon</p>        \n"
                        + "      </td>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n" + "    </tr>\n" + "    <tr>\n"
                        + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
                        + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" + "\n" + "</div></div>",
                this.personDetailsService.buildEmail("Name", "Link"));
    }
}

