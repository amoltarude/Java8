package com.stackroute.keepnote.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.NoteNotFoundException;
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
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory factory;
	@Autowired
	public NoteDAOImpl(SessionFactory sessionFactory) {
		this.factory = sessionFactory;
	}
	
	private Session getSession(){
		return factory.getCurrentSession();
	}
	/*
	 * Create a new note
	 */
	
	public boolean createNote(Note note) {
		try {
			getSession().persist(note);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * Remove an existing note
	 */
	
	public boolean deleteNote(int noteId) {
		try {
			Note temp = getNoteById(noteId);
			getSession().remove(temp);
			return true;
		} catch (NoteNotFoundException e) {
			return false;
		}	
	}

	/*
	 * Retrieve details of all notes by userId
	 */
	
	public List<Note> getAllNotesByUserId(String userId) {
		Query<Note> q = getSession().createQuery("from Note n where n.createdBy=:c_userid");
		q.setParameter("c_userid", userId);
		return q.getResultList();
	}

	/*
	 * Retrieve details of a specific note
	 */
	
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		 Note note = getSession().get(Note.class, noteId);
		 if (note==null) {
			throw new NoteNotFoundException("NOT FOUND");
		} else {
			return note;
		}
		 //return null;
	}

	/*
	 * Update an existing note
	 */

	public boolean UpdateNote(Note note) {
		try {
			getSession().merge(note);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
