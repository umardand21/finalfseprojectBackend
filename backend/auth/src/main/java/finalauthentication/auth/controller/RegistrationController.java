package finalauthentication.auth.controller;

import java.util.HashMap;


import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalauthentication.auth.exception.AuthenticationException;
import finalauthentication.auth.model.UserRequest;
import finalauthentication.auth.service.RegistrationService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins ="*")
@RequestMapping("/auth")
@RestController
public class RegistrationController {
@Autowired
private RegistrationService service;

@Autowired
private PasswordEncoder passwordEncoder;

/**
 * The method at this end point generates token for valid login credentials
 *
 * @param UserData
 * @return AuthResponse, HttpStatus
 */

@PostMapping("/authenticate")
public ResponseEntity<Map<String, String>> getAuthenticationToken(@Valid @RequestBody UserRequest userRequest)
		throws AuthenticationException {
	//log.info("Authenticating the user");
	Map<String, String> authToken = new HashMap<>();
	UserDetails user = service.loadUserByUsername(userRequest.getUsername());
	authToken.put("token", service.generateJwt(user.getUsername()));
	if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
		//log.error("Invalid credentials");
		throw new AuthenticationException("Invalid Credentials.");
	}
	//log.info("authentication successful");
	return ResponseEntity.status(HttpStatus.OK).body(authToken);
}

/**
 * The method at this end point validates token
 *
 * @param String token
 * @return AuthResponse, HttpStatus
 */

@PostMapping(value = "/authorize")
public boolean authorizeAdmin(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) {
	//log.info("validating token");
	String jwtToken = null;
	String userName = null;
	if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
		jwtToken = requestTokenHeader.substring(7);
		try {
			userName = service.getUsernameFromToken(jwtToken);
		}

		catch (SignatureException | MalformedJwtException | IllegalArgumentException | ExpiredJwtException e) {
			//log.error("invalid token");
			return false;
		}

	}
	//log.info("token is valid");
	return userName != null;

}

/*@PostMapping("/registeruser")
	public User registerUser(@RequestBody User user) throws Exception {
	String tempId=user.getUsername();
	
	if(tempId != null && !"".equals(tempId)) {
		User userObj=service.fetchUserByName(tempId);
		if(userObj != null) {
			throw new Exception("User with "+tempId+" is already exist");
		}
	}
		User userObj=null;
		userObj=service.saveUser(user);
		return userObj;
	}
@PostMapping("/login")
public User loginUser(@RequestBody User user) throws Exception {
	System.out.println(user.getUsername());
String tempId=user.getUsername();
String temppass=user.getPassword();
System.out.println(tempId);
System.out.println(temppass);
User userObj=null;
if(tempId != null && temppass !=null) {
	 userObj=service.fetchUserByNameAndPassword(tempId,temppass);
	
}
System.out.println(userObj);
if(userObj == null) {
	throw new Exception("Bad Credentials");
}
	return userObj;
}
*/

}
