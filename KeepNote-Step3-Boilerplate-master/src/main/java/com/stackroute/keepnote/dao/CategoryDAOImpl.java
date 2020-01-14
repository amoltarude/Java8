package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;

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
public class CategoryDAOImpl implements CategoryDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory factory;
	@Autowired
	public CategoryDAOImpl(SessionFactory sessionFactory) {
		this.factory = sessionFactory;
	}
	private Session getSession(){
		return factory.getCurrentSession();
	}
	/*
	 * Create a new category
	 */
	public boolean createCategory(Category category) {
		try {
			getSession().persist(category);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) {
		try {
			Category temp = getCategoryById(categoryId);
			getSession().remove(temp);
			return true;
		} catch (CategoryNotFoundException e) {
			return false;
		}	
	}
	/*
	 * Update an existing category
	 */

	public boolean updateCategory(Category category) {
		try {
			getSession().merge(category);
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	/*
	 * Retrieve details of a specific category
	 */

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {
		Category category = getSession().get(Category.class, categoryId);
		 if (category==null) {
			 
			throw new CategoryNotFoundException("CategoryNotFoundException");
		} else {
			return category;
		}
	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		Query<Category> q = getSession().createQuery("from Category n where n.categoryCreatedBy=:c_userid");
		q.setParameter("c_userid", userId);
		return q.getResultList();
	}

}
