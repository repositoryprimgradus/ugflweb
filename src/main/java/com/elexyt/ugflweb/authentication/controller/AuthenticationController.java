package com.elexyt.ugflweb.authentication.controller;


import com.elexyt.ugflweb.authentication.entity.Login;
import com.elexyt.ugflweb.authentication.entity.RoleSecurity;
import com.elexyt.ugflweb.authentication.model.AuthenticationRequest;
import com.elexyt.ugflweb.authentication.model.AuthenticationResponse;
import com.elexyt.ugflweb.cfg.JwtUtil;
import com.elexyt.ugflweb.service.UserService;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
    @Autowired
    private  UserService userService;

	@Autowired
	private JwtUtil jwtUtil;


	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
		catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		
		UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = jwtUtil.generateToken(userdetails);
        Optional<Login> login = userService.findByUsername(userdetails.getUsername());
        // Safely extract the first role's roleName if present; otherwise null
        String roleName = login.flatMap(l -> {
            if (l.getRoleSecurity() == null || l.getRoleSecurity().isEmpty()) {
                return Optional.empty();
            }
            return l.getRoleSecurity().stream().findFirst().map(RoleSecurity::getRoleName);
        }).orElse(null);

        return ResponseEntity.ok(new AuthenticationResponse(token, roleName));
	}
	

	
	@RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		// AuthenticationResponse expects (token, roleName) — roleName not available here
		return ResponseEntity.ok(new AuthenticationResponse(token, null));
	}


//
//	@RequestMapping(value = "/getUserProfile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<com.pg.hrms.model.response.UserProfileResponse> getUserProfile(Authentication auth) throws Exception{
//
//		com.pg.hrms.model.response.UserProfileResponse userProfileResponse=new com.pg.hrms.model.response.UserProfileResponse();
//		try {
//			//userProfileResponse =userService.getUserProfile(auth.getName());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//
//		}
//
//		return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
//
//
//	}
//
//


	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}



}


