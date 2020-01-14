package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.model.Note;

/*
 * This class is implementing the NoteDAO interface. This class has to be annotated with @Repository
 * annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, thus 
 * 				 clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository
@Transactional
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */
	@Autowired
	private SessionFactory factory;
	
	@Autowired
	public NoteDAOImpl(SessionFactory sessionFactory) {
			this.factory = sessionFactory;
	}

	/*
	 * Save the note in the database(note) table.
	 */

	public boolean saveNote(Note note) {
		Session session = factory.getCurrentSession();
		try {
			session.persist(note);
			return true;
		} catch (Exception e) {
			return false;	
		}
		
	}

	/*
	 * Remove the note from the database(note) table.
	 */

	public boolean deleteNote(int noteId) {
		Note temp = getNoteById(noteId);
		System.out.println(temp);
		Session session = factory.getCurrentSession();
		if (temp!=null) {
			session.delete(temp);
			session.flush();
			return true;
		} else {
			return false;
		}
	}

	/*
	 * retrieve all existing notes sorted by created Date in descending
	 * order(showing latest note first)
	 */
	public List<Note> getAllNotes() {
		Session session = factory.getCurrentSession();
		Criteria c = session.createCriteria(Note.class);
		c.addOrder(Order.asc("createdAt"));
		session.flush();
		return c.list();

	}

	/*
	 * retrieve specific note from the database(note) table
	 */
	public Note getNoteById(int noteId) {
		Session session = factory.getCurrentSession();
		Note note = session.load(Note.class, noteId);
		session.flush();
		return note;
	}

	/* Update existing note */

	public boolean UpdateNote(Note note) {
		Session session = factory.getCurrentSession();
		session.update(note);
		session.flush();
		return true;

	}

}
