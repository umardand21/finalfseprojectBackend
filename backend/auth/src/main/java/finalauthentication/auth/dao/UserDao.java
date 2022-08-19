package finalauthentication.auth.dao;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.stereotype.Repository;

import finalauthentication.auth.model.User;
import finalauthentication.auth.repository.RegistrationRepository;




@Repository
public class UserDao {
 
 @Autowired
 private RegistrationRepository userRepository;
	
 public User getUserByUsername(String username) {
	 return userRepository.findByUsername(username);
 } 
}
