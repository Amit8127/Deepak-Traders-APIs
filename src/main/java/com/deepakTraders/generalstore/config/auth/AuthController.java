package com.deepakTraders.generalstore.config.auth;

import com.deepakTraders.generalstore.config.CustomeUserDetailsService;
import com.deepakTraders.generalstore.config.JWTSecurity.JwtService;
import com.deepakTraders.generalstore.dtos.reqDtos.CreateNewUserDTO;
import com.deepakTraders.generalstore.exceptions.UserException;
import com.deepakTraders.generalstore.models.AuthRequest;
import com.deepakTraders.generalstore.models.AuthResponse;
import com.deepakTraders.generalstore.models.User;
import com.deepakTraders.generalstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomeUserDetailsService customeUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<?> createNewUser(@RequestBody CreateNewUserDTO userDto) throws UserException {

        String email = userDto.getEmail();
        String password = userDto.getPassword();

        try{
            Optional<User> isUserExistWithThisEmail = userRepository.findByEmail(email);

            if(isUserExistWithThisEmail.isPresent()) {
                throw new UserException("This Email is Already in use With Another Account");
            }

            // convert userDto to User obj by userTransformers
            User newUser = new User();
            newUser.setEmail(userDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            newUser.setFirstName(userDto.getFirstName());
            newUser.setLastName(userDto.getLastName());
            newUser.setMobile(userDto.getMobile());

            newUser = userRepository.save(newUser);

            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generateToken(authentication);

            AuthResponse authResponse = AuthResponse.builder().jwt(token).email(email).build();

            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> userLogin(@RequestBody AuthRequest authRequest) throws UserException {
        String email = authRequest.getEmail();
        String password = authRequest.getPassword();

        try{
            Authentication authentication = authentication(email, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generateToken(authentication);
            AuthResponse authResponse = AuthResponse.builder().jwt(token).email(email).build();

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private Authentication authentication(String email, String password) {
        UserDetails userDetails = customeUserDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password !!");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
