package com.elexyt.ugflweb.service;


import com.elexyt.ugflweb.authentication.entity.Login;
import com.elexyt.ugflweb.authentication.entity.Role;
import com.elexyt.ugflweb.authentication.entity.RoleSecurity;
import com.elexyt.ugflweb.authentication.repository.LoginRepository;
import com.elexyt.ugflweb.authentication.repository.RoleRepository;
import com.elexyt.ugflweb.dto.GoldRateDaliyDTO;
import com.elexyt.ugflweb.dto.PasswordChangeDto;
import com.elexyt.ugflweb.dto.UserDto;
import com.elexyt.ugflweb.dto.ValidateOtpDto;
import com.elexyt.ugflweb.entity.ClientMaster;
import com.elexyt.ugflweb.entity.ValidateOtp;
import com.elexyt.ugflweb.repository.ClientMasterRepository;
import com.elexyt.ugflweb.repository.ValidateOtpRepository;
import com.elexyt.ugflweb.utility.Mail;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ClientMasterRepository clientMasterRepository;
    @Autowired
    private ValidateOtpRepository validateOtpRepository;

    @Value("${spring.mail.username}")
    private String mailUsername;

    private final SimpleDateFormat sdfDateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Login saveOrUpdateUser(UserDto userDto) throws ParseException {
        logger.info("Request to saveOrUpdateUser: {}",userDto);
        Login login = getLogin(userDto);

        if (isDeleteAction(userDto)) {
            return deactivateUser(userDto, login);
        }

        Role role = roleRepository.findByRoleName("ROLE_ADMIN");
        updateLoginDetails(userDto, login);

        if (isUpdateAction(userDto)) {
            return updateUser(userDto, login, role);
        }

        createUser(userDto, login, role);
        return login;
    }

    public String sendOtp(String userName, String Otp) throws MessagingException, IOException {



        ClientMaster clientMaster = clientMasterRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Client master not found"));

        ValidateOtp validateOtp = validateOtpRepository.findByUserName(userName);
if(validateOtp==null) {
    validateOtp = new ValidateOtp();
}
        validateOtp.setOtp(Otp);
        validateOtp.setUserName(userName);
        validateOtp.setIsActive(1);
        validateOtpRepository.save(validateOtp);

        Mail mail = new Mail();
        mail.setFrom(mailUsername);
        mail.setMailTo(userName);
        mail.setSubject("OTP for Password Change");

        Map<String, Object> model = new HashMap<>();
        model.put("Otp", Otp);
        model.put("client", clientMaster);
        mail.setProps(model);

        return emailSenderService.sendOtp(mail);
    }
    public String validateOtp(ValidateOtpDto validateOtpDto) throws Exception {
        ValidateOtp otp=validateOtpRepository.findByUserName(validateOtpDto.getUserName());
        if(otp.getIsActive()==1&&otp.getOtp().equals(validateOtpDto.getOtp())) {
            otp.setIsActive(0);
            validateOtpRepository.save(otp);
            Login login = loginRepository.findByUsername(validateOtpDto.getUserName());
            login.setPassword(bCryptPasswordEncoder.encode(validateOtpDto.getNewPassword()));

            login.setModifiedBy(validateOtpDto.getUserName());
            login.setModifiedDate(LocalDateTime.now());
            login.setLoginId(login.getLoginId());
            loginRepository.save(login);

        }else{
            throw new Exception("Invalid OTP");
        }
        return "Password changed successfully";
    }


    public String changePassword(PasswordChangeDto passwordChangeDto) throws Exception {
        logger.info("Request to changePassword: {}",passwordChangeDto);
        Login login=loginRepository.findByUsername(passwordChangeDto.getUserName());


        if(bCryptPasswordEncoder.matches(passwordChangeDto.getOldPassword(),login.getPassword())) {
            login.setPassword(bCryptPasswordEncoder.encode(passwordChangeDto.getNewPassword()));

            login.setModifiedBy(passwordChangeDto.getUserName());
            login.setModifiedDate(LocalDateTime.now());
            login.setLoginId(login.getLoginId());
             return "Password Changed Successfully";
        }else {
            return "OldPassword Mismatch";
        }

    }

    private Login getLogin(UserDto userDto) {
        if (userDto.getLoginId()!=null) {
            return loginRepository.findByLoginId(userDto.getLoginId());
        }
        return new Login();
    }

    private boolean isDeleteAction(UserDto userDto) {
        return userDto.getActionType().equalsIgnoreCase("DELETE");
    }

    private Login deactivateUser(UserDto userDto, Login login) {
        login.setModifiedBy(userDto.getUsername());
        login.setModifiedDate(LocalDateTime.now());
        login.setIsActive(0);
        return loginRepository.save(login);
    }

    private boolean isUpdateAction(UserDto userDto) {
        return userDto.getActionType().equalsIgnoreCase("UPDATE");
    }

    private Login updateUser(UserDto userDto, Login login, Role role) {
        login.setModifiedBy(userDto.getUsername());
        login.setModifiedDate(LocalDateTime.now());
        loginRepository.save(login);

        return login;
    }

    private void updateLoginDetails(UserDto userDto, Login login) throws ParseException {
        login.setUsername(userDto.getEmail());
        login.setName(userDto.getName());
        login.setEmail(userDto.getEmail());
        login.setPhone(userDto.getPhone());

    }

    private void createUser(UserDto userDto, Login login, Role role) {
        RoleSecurity roleSecurity=new RoleSecurity();
        roleSecurity.setRoleName(role.getRoleName());
        roleSecurity.setRoleId(role.getRoleId());

        login.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        login.setRoleSecurity(Set.of(roleSecurity));
        login.setIsActive(1);
        login.setCreatedBy(userDto.getEmail());
        login.setCreatedDate(LocalDateTime.now());
        loginRepository.save(login);

    }


    public Boolean checkEmailExist(String email) throws Exception {
        return loginRepository.existsByEmail(email);
    }

    public Login userDetails(String  loginId)throws Exception{
        logger.info("Request to userDetails: {}",loginId);
        return loginRepository.findByLoginId(loginId);

    }

    @Transactional(readOnly = true)
    public Optional<Login> findByUsername(String username) {

        return loginRepository.findByUsername(username)!=null ? Optional.of(loginRepository.findByUsername(username)) : Optional.empty();
    }




}
