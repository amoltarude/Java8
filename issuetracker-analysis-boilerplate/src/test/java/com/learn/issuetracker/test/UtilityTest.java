package com.learn.issuetracker.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.learn.issuetracker.model.Employee;
import com.learn.issuetracker.model.Issue;
import com.learn.issuetracker.repository.EmployeeRepository;
import com.learn.issuetracker.repository.Utility;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmployeeRepository.class)
public class UtilityTest {

	@Test
	public void testParseEmployee() {
		String input = "102,Ankur,Delhi";
		Employee employee = Utility.parseEmployee(input);
		assertEquals("location is not parsed correctly", "Delhi", employee.getLocation());
		assertEquals("Id is not parsed correctly", 102, employee.getEmplId());
	}

	@Test
	public void testParseIssueWithExistingEmployee() {
		String input = "IS002,issue summary2,01/04/2019,15/04/2019,MEDIUM,CLOSED,102";
		PowerMockito.mockStatic(EmployeeRepository.class);
		when(EmployeeRepository.getEmployee(102)).thenReturn(Optional.of(new Employee(102, "Test", "Mysore")));
		Issue issue = Utility.parseIssue(input);
		assertNotNull("Employee should to be null", issue.getAssignedTo());
		assertEquals("location of Assigned employee not set correctly or is null", "Mysore",
				issue.getAssignedTo().getLocation());
		assertEquals(" Issue ID is not parsed correctly", "IS002", issue.getIssueId());
	}

}