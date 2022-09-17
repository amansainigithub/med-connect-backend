package com.med.connect.controllers;


import com.med.connect.config.EmailConfiguration;
import com.med.connect.config.SimpleEmailConfiguration;
import com.med.connect.constants.admin.UrlMappings;
import com.med.connect.domain.Role;
import com.med.connect.domain.User;
import com.med.connect.enums.ERole;
import com.med.connect.helper.GenerateUniqueUserNameViaPublic;
import com.med.connect.helper.OsInformation;
import com.med.connect.payload.request.*;
import com.med.connect.payload.response.JwtResponse;
import com.med.connect.payload.response.MessageResponse;
import com.med.connect.repository.RoleRepository;
import com.med.connect.repository.UserRepository;
import com.med.connect.security.jwt.JwtUtils;
import com.med.connect.security.services.UserDetailsImpl;
import com.med.connect.utils.ResponseGenerator;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@ApiOperation("Api for Authentication")
@Slf4j
@RequestMapping(UrlMappings.AUTH_BASE_URL)
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private SimpleEmailConfiguration simpleEmailConfiguration;

  @Autowired
  private GenerateUniqueUserNameViaPublic generateUniqueUserNameViaPublic;

  private HttpSession session;

  @PostMapping(UrlMappings.SIGN_IN)
  @ApiOperation(value = "Api for Authenticate")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  @PostMapping(UrlMappings.SIGN_UP)
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    user.setFirstname(signUpRequest.getFirstname());
    user.setSurname(signUpRequest.getSurname());


    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);



    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }








  //########  SIGN UP USER IN PUBLIC VIA ROLE --> NORMAL-USER , DOCTOR , STUDENT  ##########
  @PostMapping(UrlMappings.SIGN_UP_USER)
  @ApiOperation(value = "Api for Authenticate SignUp User Public")
  public ResponseEntity<?> signUpUser(@Valid @RequestBody SignUpRequestPublic  signUpRequestPublic , HttpServletRequest request) {
    String userName =  this.generateUniqueUserNameViaPublic.generateUniqueUserNameViaPublic(signUpRequestPublic);

    //Set UserName
    signUpRequestPublic.setUsername(userName);

    if (userRepository.existsByEmail(signUpRequestPublic.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    if (userRepository.existsByUsername(signUpRequestPublic.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }


    // Create new user's account
    User user = new User(signUpRequestPublic.getUsername(),
                  signUpRequestPublic.getEmail(),
                  encoder.encode(signUpRequestPublic.getPassword()));

    user.setFirstname(signUpRequestPublic.getFirstname());
    user.setSurname(signUpRequestPublic.getSurname());

    Set<String> strRoles = signUpRequestPublic.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "STUDENT":
            Role adminRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          case "DOCTOR":
            Role modRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);

            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);

    //Save OS Details [Operating System Details]
    OsInformation.getUserOS(request,user);

    //Generate Email Token
    String token = GenerateUniqueUserNameViaPublic.generateUUID();

    user.setEmailToken(token);
    userRepository.save(user);

    //Creating Email Verify Email Link
    String emailVerifyLink = "<a href=http://localhost:8080/med-connect/api/auth/verifyTokenEmail?user="+user.getEmail()+"&token="+token+">Conformation Link </a>";

    //Send Email Conformation Link
    this.simpleEmailConfiguration.sendSimpleEmail(user.getEmail(),emailVerifyLink , "Please Conform your Email");

    log.info("::::::::: {}     User registered successfully ! But Email Verify is Mandatory !!! ");
    return ResponseEntity.ok(new MessageResponse("User registered successfully ! But Email Verify is Mandatory !!! "));
  }


//##################################### SIGN IN USER PUBLIC ############################################
  @PostMapping(UrlMappings.SIGN_IN_USER)
  @ApiOperation(value = "Api for Authenticate SignIn User Public")
  public ResponseEntity<?> signInUser(@Valid @RequestBody LoginRequest loginRequest) {
    User user =  userRepository.findByEmail(loginRequest.getUsername())
                          .orElseThrow(()-> new UsernameNotFoundException("Uer Not Found user : " + loginRequest.getUsername()));

    if(!user.getEmailVerified())
    {
      throw new RuntimeException("Email Not Verify ! Please Verify your Email-Id");
    }

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }



  //##################################### FORGET PASSWORD ############################################
  @PostMapping(UrlMappings.FORGOT_PASSWORD)
  @ApiOperation(value = "Api for forget password")
  public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgetPassword forgetPassword, HttpServletRequest request) {
          try {
              this.session  = request.getSession();
            if(forgetPassword.getEmail() == null || forgetPassword.getEmail().isEmpty())
            {
              return ResponseEntity
                      .badRequest()
                      .body(new MessageResponse("Please Enter Correct email id : " + forgetPassword.getEmail()));
            }

            Optional<User> optional =  this.userRepository.findByEmail(forgetPassword.getEmail());
            if(!optional.isPresent())
            {
              return ResponseEntity
                      .badRequest()
                      .body(new MessageResponse("email-id Not Found : " + forgetPassword.getEmail()));
            }
            else {
              //  IF EMAIL ID IS FOUND

               //String randUuId = UUID.randomUUID().toString();
              String otp = String.format("%04d", new Random().nextInt(10000));

              //set session to key and userName
               session.setAttribute(optional.get().getEmail() , otp );

               //Session Expire in 10 minutes..
               session.setMaxInactiveInterval(10 * 60);

              //Send Email
              EmailConfiguration.sendEmail("ishumessi2@gmail.com",otp);

              System.out.println("After otp");
              System.out.println(session.getAttribute(forgetPassword.getEmail()));

              return ResponseEntity.ok(new MessageResponse("success"));
            }
          }
          catch (Exception e)
          {
              e.getStackTrace();
              return ResponseGenerator.generateBadRequestResponse(e.getMessage());
          }
  }




  @PostMapping(UrlMappings.FORGOT_PASSWORD_OTP_VERIFY)
  @ApiOperation(value = "Api for OTP VERIFY and forget password ")
  public ResponseEntity<?> forgotPasswordOtpVerify(@Valid @RequestBody ForgetPasswordOtp forgetPasswordOtp, HttpServletRequest request) {
    try {
      System.out.println(forgetPasswordOtp.toString());
      if(forgetPasswordOtp.getOtp() == null || forgetPasswordOtp.getOtp().isEmpty()
         ||  forgetPasswordOtp.getEmail() == null || forgetPasswordOtp.getEmail().isEmpty()
         || forgetPasswordOtp.getNewPassword()== null || forgetPasswordOtp .getConformPassword().isEmpty()
         || forgetPasswordOtp.getConformPassword() == null || forgetPasswordOtp.getConformPassword().isEmpty())
      {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Field is Mandatory !!!"));
      }


      if(!forgetPasswordOtp.getNewPassword().equals(forgetPasswordOtp.getConformPassword()))
      {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Password not matched !!!"));
      }

       String sessionOtp =  (String)session.getAttribute(forgetPasswordOtp.getEmail());
       log.info("sessionOtp ::::::::::::  {} " + sessionOtp );

      if(sessionOtp.equals(forgetPasswordOtp.getOtp()) && forgetPasswordOtp.getNewPassword().equals(forgetPasswordOtp.getConformPassword())) {

        log.info("OTP IS VERIFIED ::::::::::::::::::::: {} :: = >  Success ");
        log.info("password and conform password is matched success::::::::::::  {} ");

        User user =  this.userRepository.findByEmail(forgetPasswordOtp.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User Not found !!"));

        user.setPassword(encoder.encode(forgetPasswordOtp.getConformPassword()));

        this.userRepository.save(user);

        //removing session value
        session.removeAttribute(forgetPasswordOtp.getEmail());

        //Checking session value is found
        if(session.getAttribute(forgetPasswordOtp.getEmail()) != null)
        {
          log.info("Session Value Not removed  :::: {} :: = >  Failed ");
        }
        else {
          log.info("session value is successfully removed :::: {} :: = >  Success ");
        }

        return ResponseEntity.ok(new MessageResponse("Password Changed Success"));
      }
      else {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("something went wrong !!!"));
      }
    }
    catch (Exception e)
    {
      e.getStackTrace();
      return ResponseGenerator.generateBadRequestResponse(e.getMessage());
    }
  }



  //########## EMAIL VARIFICATION TOKEN###########
  @GetMapping(UrlMappings.VERIFY_TOKEN_EMAIL)
  @ApiOperation(value = "Verify Token Email")
  public ResponseEntity<?> forgotPassword(@RequestParam String user , @RequestParam String token , HttpServletResponse response) {
          log.info("USER       :::::::::  " + user);
          log.info("TOKEN      :::::::::   " + token);

          if(token.trim() == null || token.trim().isEmpty())
          {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Token is Empty | Something went wrong"));
          }

          //get User By Token
          User currentUser =  this.userRepository.findByEmailToken(token.trim()).orElseThrow(()-> new UsernameNotFoundException("Token Not found !!"));

          if(currentUser.getEmailToken().equals(token.trim()))
          {
                currentUser.setEmailVerified(true);

                //save User is email verified
                this.userRepository.save(currentUser);

                try {
                  log.info(":::::::::::::::::    re-direct To success Page" );
                  response.sendRedirect("http://localhost:4200/email-verify-success");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return ResponseEntity.ok().body(new MessageResponse("Email is Verified successfully"));
          }

             return ResponseEntity
                      .badRequest()
                      .body(new MessageResponse("Token is Empty | Something went wrong"));
          }



          //#######################    RESEND EMAIL LINK #############################

  //########## EMAIL VARIFICATION TOKEN###########
  @PostMapping(UrlMappings.RESEND_EMAIL_LINK)
  @ApiOperation(value = "Resend Email Link")
  public ResponseEntity<?> resendEmailLink(@Valid @PathVariable String  username , HttpServletResponse response) {
        log.info("EMAIL  ::::::::::::  " + username);

        if(username.trim() == null || username.trim().isEmpty())
        {
          return ResponseEntity
                  .badRequest()
                  .body(new MessageResponse("Token is Empty | Something went wrong"));
        }


        User currentUser = this.userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found Here"));

        //Check If Email Id is Already Verified

        if(!currentUser.getEmailVerified())
        {
          return ResponseEntity
                  .badRequest()
                  .body(new MessageResponse("Email id is Already Verified !!"));
        }

        //generate New Token
        String token =  GenerateUniqueUserNameViaPublic.generateUUID();

        //Set Token
        currentUser.setEmailToken(token);

        //save User is email verified
        this.userRepository.save(currentUser);

      //Creating Email Verify Email Link
      String emailVerifyLink = "<a href=http://localhost:8080/med-connect/api/auth/verifyTokenEmail?user="+currentUser.getEmail()+"&token="+token+">Conformation Link </a>";

      //Send Email Conformation Link
      this.simpleEmailConfiguration.sendSimpleEmail(currentUser.getEmail(),emailVerifyLink , "Please Conform your Email");

      log.info("::::::::: {}     User registered successfully ! But Email Verify is Mandatory !!! ");
      return ResponseEntity.ok(new MessageResponse("Email link Resend successfully "));

  }





}
