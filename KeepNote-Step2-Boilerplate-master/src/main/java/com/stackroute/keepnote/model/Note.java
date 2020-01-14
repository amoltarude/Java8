package com.stackroute.keepnote.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/*
 * The class "Note" will be acting as the data model for the note Table in the database. Please
 * note that this class is annotated with @Entity annotation. Hibernate will scan all package for 
 * any Java objects annotated with the @Entity annotation. If it finds any, then it will begin the 
 * process of looking through that particular Java object to recreate it as a table in your database.
 */

@Component
@Entity
@Table(name="note")
public class Note {

	@Id
	@Column(name="note_id")
	private int noteId;
	@Column(name="note_title")
	private String noteTitle;
	@Column(name="note_content")
	private String noteContent;
	@Column(name="note_status")
	private String noteStatus;
	@Column(name="note_creation_date")
	private LocalDateTime createdAt;
	
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Note() {

	}

	public Note(int i, String string, String string2, String string3, LocalDateTime localDate) {
		this.noteId = i;
		this.noteTitle = string;
		this.noteContent = string2;
		this.noteStatus = string3;
		this.createdAt = localDate;
	}

	public int getNoteId() {

		return this.noteId;
	}

	public String getNoteTitle() {

		return this.noteTitle;
	}

	public String getNoteContent() {

		return this.noteContent;
	}

	public String getNoteStatus() {

		return this.noteStatus;
	}

	public void setNoteId(int parseInt) {
		this.noteId = parseInt;
	}

	public void setNoteTitle(String parameter) {
		this.noteTitle = parameter;
	}

	public void setNoteContent(String parameter) {
		this.noteContent = parameter;
	}

	public void setNoteStatus(String parameter) {
		this.noteStatus = parameter;
	}

	public void setCreatedAt(LocalDateTime now) {
		this.createdAt = now;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", noteTitle=" + noteTitle + ", noteContent=" + noteContent + ", noteStatus="
				+ noteStatus + ", createdAt=" + createdAt + "]";
	}

}
