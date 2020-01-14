package com.stackroute.keepnote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.dao.CategoryDAO;
import com.stackroute.keepnote.dao.NoteDAO;
import com.stackroute.keepnote.dao.ReminderDAO;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn�t currently 
* provide any additional behavior over the @Component annotation, but it�s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteDAO,CategoryDAO,ReminderDAO.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	/*
	 * This method should be used to save a new note.
	 */
	@Autowired
	private NoteDAO noteDao;
	@Autowired
	private CategoryDAO categoryDao;
	@Autowired
	private ReminderDAO reminderDao;
	@Autowired
	public NoteServiceImpl(NoteDAO noteDAO, CategoryDAO categoryDAO, ReminderDAO reminderDAO) {
		super();
		this.noteDao = noteDAO;
		this.categoryDao = categoryDAO;
		this.reminderDao = reminderDAO;
	}	
	
	public boolean createNote(Note note) throws ReminderNotFoundException, CategoryNotFoundException {
		Reminder reminder = note.getReminder();
		Category category = note.getCategory();
		try {
			if(reminder!=null) 
				reminderDao.getReminderById(reminder.getReminderId());
		}catch(ReminderNotFoundException rnf) {
			throw new ReminderNotFoundException("ReminderNotFoundException");
		}
		try {
			if(category!=null)
				categoryDao.getCategoryById(category.getCategoryId());
		}catch(CategoryNotFoundException cnf) {
			throw new  CategoryNotFoundException("CategoryNotFoundException");
		}
		
		return noteDao.createNote(note);

	}

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(int noteId) {
		return noteDao.deleteNote(noteId);
	}
	/*
	 * This method should be used to get a note by userId.
	 */

	public List<Note> getAllNotesByUserId(String userId) {
		return noteDao.getAllNotesByUserId(userId);

	}

	/*
	 * This method should be used to get a note by noteId.
	 */
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		Note note = noteDao.getNoteById(noteId);
		if(note==null)
			throw new NoteNotFoundException("NoteNotFoundException");
		else
		return note;

	}

	/*
	 * This method should be used to update a existing note.
	 */

	public Note updateNote(Note note, int id)
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {
		Note noteFound = noteDao.getNoteById(id);
		Reminder reminder = note.getReminder();
		Category category = note.getCategory();
		if(noteFound==null) {
			throw new NoteNotFoundException("NoteNotFoundException");
		}else {
			noteDao.UpdateNote(noteFound);
		}
		try {
			if(reminder!=null) 
				reminderDao.getReminderById(reminder.getReminderId());
		}catch(ReminderNotFoundException rnf) {
			rnf.printStackTrace();
		}
		try {
			if(category!=null)
				categoryDao.getCategoryById(category.getCategoryId());
		}catch(CategoryNotFoundException cnf) {
			cnf.printStackTrace();
		}
		
		return note;
	}

}
