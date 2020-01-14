package com.stackroute.keepnote.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository
@Transactional
public class UserDaoImpl implements UserDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory factory;
	@Autowired
	public UserDaoImpl(SessionFactory sessionFactory) {
		this.factory = sessionFactory;
	}

	private Session getSession(){
		return factory.getCurrentSession();
	}
	/*
	 * Create a new user
	 */

	public boolean registerUser(User user) {
			try {
				getSession().persist(user);
				return true;
			} catch (Exception e) {
				return false;
			}
	}

	/*
	 * Update an existing user
	 */

	public boolean updateUser(User user) {
		try {
			getSession().merge(user);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/*
	 * Retrieve details of a specific user
	 */
	public User getUserById(String UserId) {
		//User user;
		try {
			return getSession().get(User.class, UserId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		return null;
		}
	//	 return user;
	}

	/*
	 * validate an user
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		User temp = getUserById(userId);
		if (temp!=null) {
			if (temp.getUserId().equals(userId) && temp.getUserPassword().equals(password)) {
				return true;
			} else {
				
				return false;
			}
		} else {
			throw new UserNotFoundException("UserNotFoundException");
		//	return false;
		}
	}

	/*
	 * Remove an existing user
	 */
	public boolean deleteUser(String userId) {
		try {
			User temp = getUserById(userId);
			getSession().remove(temp);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
