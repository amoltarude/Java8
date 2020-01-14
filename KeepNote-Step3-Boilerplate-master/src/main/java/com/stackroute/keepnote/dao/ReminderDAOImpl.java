package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Reminder;

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
public class ReminderDAOImpl implements ReminderDAO {
	
	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory factory;
	@Autowired
	public ReminderDAOImpl(SessionFactory sessionFactory) {
		this.factory = sessionFactory;
	}
	private Session getSession(){
		return factory.getCurrentSession();
	}
	/*
	 * Create a new reminder
	 */

	public boolean createReminder(Reminder reminder) {
		try {
			getSession().persist(reminder);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/*
	 * Update an existing reminder
	 */

	public boolean updateReminder(Reminder reminder) {
		try {
			getSession().merge(reminder);
			return true;
		} catch (Exception e) {
			return false;
		}


	}

	/*
	 * Remove an existing reminder
	 */
	
	public boolean deleteReminder(int reminderId) {
		try {
			Reminder temp = getReminderById(reminderId);
			getSession().remove(temp);
			return true;
		} catch (ReminderNotFoundException e) {
			return false;
		}	

	}

	/*
	 * Retrieve details of a specific reminder
	 */
	
	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {
		Reminder category = getSession().get(Reminder.class, reminderId);
		 if (category==null) {
			throw new ReminderNotFoundException("NOT FOUND");
		} else {
			return category;
		}
	}

	/*
	 * Retrieve details of all reminders by userId
	 */
	
	public List<Reminder> getAllReminderByUserId(String userId) {
		Query<Reminder> q = getSession().createQuery("from Reminder n where n.reminderCreatedBy=:c_userid");
		q.setParameter("c_userid", userId);
		return q.getResultList();
	}

}
