package com.med.connect.security.services;

import com.med.connect.domain.User;
import com.med.connect.messageResp.MessageResponse;
import com.med.connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    //############################  CHECK BY USER-NAME ##################################
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);

  }

}






//    Optional<User> user = userRepository.findByUsername(username);
//    if(user.isPresent())
//    {
//      return UserDetailsImpl.build(user.get());
//    } else if (!user.isPresent()) {
//
//      User userByEmail = userRepository.findByEmail(user.get().getEmail())
//              .orElseThrow(() -> new UsernameNotFoundException("Email or UserName Not Found with username: " + username));
//      return UserDetailsImpl.build(userByEmail);
//    }
//    else {
//      throw new RuntimeException("SOME THING WENT WRONG");
//
//    }