package com.jwd.controller;

import com.jwd.helper.JwtUtil;
import com.jwd.model.JwtRequst;
import com.jwd.model.JwtResponse;
import com.jwd.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** @noinspection ALL*/
@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody JwtRequst jwtRequst) throws Exception {
        System.out.println(jwtRequst);

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequst.getUsername(),jwtRequst.getPassword()));
        }catch (UsernameNotFoundException e){;
            e.printStackTrace();
            throw new Exception(" Bad credentials");

        }
        //fine area...
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequst.getUsername());

        String token = this.jwtUtil.generateToken(userDetails);
        System.out.println("JWT"+token);

        //{"token":"value"}
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
