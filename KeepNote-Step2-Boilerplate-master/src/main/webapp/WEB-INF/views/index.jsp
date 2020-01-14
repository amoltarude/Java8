
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>KeepNote</title>
</head>
<body>
	<!-- Create a form which will have text boxes for Note ID, title, content and status along with a Send 
		 button. Handle errors like empty fields -->
		<form:form method="post" modelAttribute="note" action="add">
		<label>Id:</label> <form:input name="noteId"   path="noteId"/> 
		<br/><br/>
		<label>Title:</label> <form:input name="noteTitle" path="noteTitle"/> 
		<br/><br/>
		<label>Content:</label> <form:input name="noteContent" path="noteContent"/> 
		<br/><br/>
		<label>Status:</label> <form:input name="noteStatus" path="noteStatus"/> 
		<br/><br/>
		<button type="submit">Submit</button> 
	</form:form> 
<a type="button" href="/update">update</a>
	<!-- display all existing notes in a tabular structure with Id, Title,Content,Status, Created Date and Action -->
	
		<table border="1">
		<tr style="background-color: red;">
			<td>Id</td>
			<td>Title</td>
			<td>Content</td>
			<td>Status</td>
			<td>Date</td>
			<td>Action</td>
		</tr>
		<c:forEach var="note" items="${notes}">
			<tr>
				<td>${note.noteId}</td>
				<td>${note.noteTitle}</td>
				<td>${note.noteContent}</td>
				<td>${note.noteStatus}</td>
				<td>${note.createdAt}</td>
				<td><a type="button"
							href="./delete?noteId=${note.noteId}">Delete</a></td>
	
			</tr>
		</c:forEach>
	</table>
</body>

</html>