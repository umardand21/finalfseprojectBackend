package finalauthentication.auth.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import finalauthentication.auth.dao.UserDao;
import finalauthentication.auth.model.MyUserDetail;
import finalauthentication.auth.model.User;
import finalauthentication.auth.repository.RegistrationRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class RegistrationService implements UserDetailsService {
	@Autowired
private UserDao userDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RegistrationRepository repo;
	
	public User saveUser(User user) {
		return repo.save(user);
	}

	//public User fetchUserByName(String name) {
		//return RegistrationRepository.findByUsername(name);
		
	//}
	//public User fetchUserByNameAndPassword(String name,String pass) {
		//return repo.findByusernameAndPassword(name,pass);
		
	//}

	public UserDetails loadUserByUsername(String username) {
		//log.info("getting user from username");
		User user = userDao.getUserByUsername(username);
		if (user == null) {
			//log.warn("no users available");
			throw new UsernameNotFoundException("No Users Available With " + username + " !!!....");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		//log.info("user found with the provided username");
		return new MyUserDetail(user);
	}

	public String generateJwt(String username) {
	//	log.info("generating token");
		JwtBuilder builder = Jwts.builder();
		builder.setSubject(username);
		builder.setIssuedAt(new Date());
		builder.setExpiration(new Date(new Date().getTime() + (30 * 60 * 1000)));
		builder.signWith(SignatureAlgorithm.HS256, "secretkey");
		//log.info("token generation successful");
		return builder.compact();
	}

	public String getUsernameFromToken(String jwtToken) {
		//log.info("extracting username from token");
		Jws<Claims> jws = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(jwtToken.replace("Bearer ", ""));
		//log.info("username extracted from token sucessfully");
		return jws.getBody().getSubject();
	}
	
	
}
