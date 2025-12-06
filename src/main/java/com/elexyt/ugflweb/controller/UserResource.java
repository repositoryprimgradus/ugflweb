package com.elexyt.ugflweb.controller;


import com.elexyt.ugflweb.authentication.entity.Login;
import com.elexyt.ugflweb.authentication.repository.LoginRepository;
import com.elexyt.ugflweb.dto.GoldRateDaliyDTO;
import com.elexyt.ugflweb.dto.PasswordChangeDto;
import com.elexyt.ugflweb.dto.UserDto;
import com.elexyt.ugflweb.dto.ValidateOtpDto;
import com.elexyt.ugflweb.error.BadRequestAlertException;
import com.elexyt.ugflweb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/user")
public class UserResource {



    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "user";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;


    private final LoginRepository loginRepository;

    public UserResource(
            UserService userService,
            LoginRepository loginRepository
    ) {
        this.userService = userService;
        this.loginRepository = loginRepository;
    }






    @PostMapping("")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto, Authentication auth) throws Exception {


        Boolean emailExist = userService.checkEmailExist(userDto.getEmail());


        if (userDto.getActionType().equalsIgnoreCase("UPDATE")) {
            Login user = userService.userDetails(userDto.getLoginId());

            if (user == null) {
                throw new BadRequestAlertException("User not found", ENTITY_NAME, "usernotfound");
            }

            if (!user.getEmail().equalsIgnoreCase(userDto.getEmail()) && emailExist) {
                throw new BadRequestAlertException("Email already register", ENTITY_NAME, "emailexists");
            }

            Login login = userService.saveOrUpdateUser(userDto);
            userDto.setPassword("********");
            userDto.setLoginId(login.getLoginId());

            return ResponseEntity.created(new URI("/api/user/" + userDto.getLoginId()))
                    .headers(
                            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDto.getLoginId().toString())
                    )
                    .body(userDto);

        }


        if (userDto.getActionType().equalsIgnoreCase("INSERT")) {

            if (emailExist) {
                throw new BadRequestAlertException("Email already register", ENTITY_NAME, "emailexists");
            }

            Login login = userService.saveOrUpdateUser(userDto);
            userDto.setPassword("********");
            userDto.setLoginId(login.getLoginId());

            return ResponseEntity.created(new URI("/api/user/" + userDto.getLoginId()))
                    .headers(
                            HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userDto.getLoginId().toString())
                    )
                    .body(userDto);


        }

        if (userDto.getActionType().equalsIgnoreCase("DELETE")) {


            Login login = userService.saveOrUpdateUser(userDto);
            return ResponseEntity.noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, login.toString()))
                    .build();
        }


        throw new BadRequestAlertException("Invalid action type", ENTITY_NAME, "invalidactiontype");
    }


    @RequestMapping(value = "/changePasswordOtp", method = RequestMethod.POST)
    public ResponseEntity<String> changePassword(@RequestParam("username") String userName) throws Exception {

        Login login = loginRepository.findByUsername(userName);

  if(login==null || login.getEmail()==null || login.getEmail().isEmpty()){
      throw new BadRequestAlertException("User not found", ENTITY_NAME, "usernotfound");

  }

        String otp = generateOTP(6);
        userService.sendOtp(userName, otp);

        return ResponseEntity.created(new URI("/api/user/" + userName))
                .headers(
                        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userName)
                )
                .body("Otp sent successfully to registered email");


    }


    @RequestMapping(value = "/validateOtp", method = RequestMethod.POST)
    public ResponseEntity<String> validateOtp(@RequestBody ValidateOtpDto validateOtpDto) throws Exception {


        String message =userService.validateOtp(validateOtpDto);

        return ResponseEntity.created(new URI("/api/user/" + validateOtpDto.getUserName()))
                .headers(
                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validateOtpDto.getUserName())
                )
                .body(message);


    }



    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) throws Exception {
        String message =userService.changePassword(passwordChangeDto);

        return ResponseEntity.created(new URI("/api/user/" + passwordChangeDto.getUserName()))
                .headers(
                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passwordChangeDto.getUserName())
                )
                .body(message);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Login> getUser(@PathVariable("username") String username) {
        LOG.debug("REST request to get user : {}", username);
        Optional<Login> login = userService.findByUsername(username);
        return ResponseUtil.wrapOrNotFound(login);
    }

    private static String generateOTP(int length) {
        String numbers = "1234567890";
        Random random = new Random();
        char[] otp = new char[length];

        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        String otpStr = new String(otp);

        return otpStr;
    }



}
