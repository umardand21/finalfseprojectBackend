package finalauthentication.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import finalauthentication.auth.model.User;

@Repository
public interface RegistrationRepository extends CrudRepository<User,Long>{



	  User findByUsername(String username);
	

}
