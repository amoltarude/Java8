package com.learn.issuetracker.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.learn.issuetracker.model.Employee;
import com.learn.issuetracker.model.Issue;
import com.learn.issuetracker.repository.IssueRepositoryImpl;
import com.learn.issuetracker.service.IssueTrackerService;
import com.learn.issuetracker.service.IssueTrackerServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class IssueTrackerServiceImplTest {

	@Mock
	private IssueRepositoryImpl dao;

	private IssueTrackerService service;
	private List<Issue> issueList;

	@Before
	public void setUp() throws Exception {
		issueList = Arrays.asList(new Issue[] {
				new Issue("ISS01", "Issue Summary 01", LocalDate.parse("2019-04-10"), LocalDate.parse("2019-04-17"),
						"HIGH", "CLOSED", new Employee(101, "John", "Bangalore")),
				new Issue("ISS02", "Issue Summary 02", LocalDate.parse("2019-04-01"), LocalDate.parse("2019-04-15"),
						"MEDIUM", "CLOSED", new Employee(102, "Jack", "Bangalore")),
				new Issue("ISS03", "Issue Summary 03", LocalDate.parse("2019-04-01"), LocalDate.parse("2019-04-15"),
						"MEDIUM", "OPEN", new Employee(102, "Jack", "Bangalore")),
				new Issue("ISS04", "Issue Summary 04", LocalDate.parse("2019-04-01"), LocalDate.parse("2019-04-21"),
						"LOW", "OPEN", new Employee(105, "Mike", "Mangalore")),
				new Issue("ISS05", "Issue Summary 05", LocalDate.parse("2019-04-25"), LocalDate.parse("2019-05-16"),
						"LOW", "OPEN", new Employee(106, "David", "Mysore")),
				new Issue("ISS06", "Issue Summary 06", LocalDate.parse("2019-04-25"), LocalDate.parse("2019-05-07"),
						"MEDIUM", "OPEN", new Employee(103, "Mavis", "Hubli")),
				new Issue("ISS07", "Issue Summary 07", LocalDate.parse("2019-04-25"), LocalDate.parse("2019-05-02"),
						"HIGH", "OPEN", new Employee(104, "Zita", "Mandya")),
				new Issue("ISS08", "Issue Summary 08", LocalDate.parse("2019-04-25"), LocalDate.parse("2019-05-16"),
						"LOW", "OPEN", new Employee(106, "David", "Mysore")),
				new Issue("ISS09", "Issue Summary 09", LocalDate.parse("2019-04-02"), LocalDate.parse("2019-04-17"),
						"MEDIUM", "CLOSED", new Employee(103, "Mavis", "Hubli")),
				new Issue("ISS10", "Issue Summary 10", LocalDate.parse("2019-04-02"), LocalDate.parse("2019-05-04"),
						"HIGH", "CLOSED", new Employee(104, "Zita", "Mandya")),
				new Issue("ISS11", "Issue Summary 11", LocalDate.parse("2019-04-02"), LocalDate.parse("2019-04-23"),
						"LOW", "CLOSED", new Employee(106, "David", "Mysore"))

		});
		service = new IssueTrackerServiceImpl(dao);
		when(dao.getIssues()).thenReturn(issueList);
	}

	@After
	public void tearDown() throws Exception {
		service = null;
	}

	@Test
	public void testGetClosedIssueCount() {
		assertEquals("Count of closed issues don't match", service.getClosedIssueCount(), 5);
	}

	@Test
	public void testgetIssueByIdExisting() {
		Issue issue = service.getIssueById("ISS05");
		assertNotNull("Existing issue was not found", issue);
		assertEquals("Summary of the issue doesn't match", "Issue Summary 05", issue.getSummary());
	}

	@Test
	public void testgetIssueAssignedToExisting() {
		Optional<Employee> employee = service.getIssueAssignedTo("ISS10");
		assertTrue("Employee object should be present in the optional for assigned employees", employee.isPresent());
		assertEquals("Name of the employee assigned to issue doesn't match", "Zita", employee.get().getName());
	}

	@Test
	public void testgetOpenIssuesInExpectedResolutionOrder() {
		Set<String> openIssues = service.getOpenIssuesInExpectedResolutionOrder();
		assertNotNull("The open issues set should not be null", openIssues);
		assertEquals("[ISS03, ISS04, ISS07, ISS06, ISS05, ISS08]", openIssues.toString());
	}

	@Test
	public void testgetOpenIssuesOrderedByPriorityAndResolutionDate() {
		List<Issue> issues = service.getOpenIssuesOrderedByPriorityAndResolutionDate();
		assertNotNull("The open issues List should not be null", issues);
		assertEquals("The size of the List containing open issues should be 6", 6, issues.size());
		assertEquals("The sorted order is in correct", "ISS07", issues.get(5).getIssueId());
		assertEquals("The sorted order is in correct", "ISS05", issues.get(3).getIssueId());
	}

	@Test
	public void testGetOpenIssuesDelayedbyEmployees() {
		List<String> delayed = service.getOpenIssuesDelayedbyEmployees();
		assertNotNull("List of delayed Issues cannot be null", delayed);
		assertEquals("The number of distinct employees who delayed the issues is in correct", 2, delayed.size());
	}

	@Test
	public void getHighPriorityOpenIssueAssignedTo() {
		Map<String, Integer> prIssues = service.getHighPriorityOpenIssueAssignedTo();
		assertNotNull("Map of high priority open issues should not be null", prIssues);
		assertEquals("Size of Map of high priority open issues should be 1", 1, prIssues.size());
	}

	@Test
	public void getOpenIssuesCountGroupedbyPriority() {
		Map<String, Long> prIssues = service.getOpenIssuesCountGroupedbyPriority();
		assertNotNull("Map of high priority open issues should not be null", prIssues);
		assertEquals("Size of Map of high priority open issues should be 3", 3, prIssues.size());
	}

	@Test
	public void testGetOpenIssuesGroupedbyPriority() {
		Map<String, List<Issue>> prIssues = service.getOpenIssuesGroupedbyPriority();
		assertNotNull("Map of high priority open issues should not be null", prIssues);
		assertEquals("Size of Map of high priority open issues should be 3", 3, prIssues.size());
		assertEquals("Size of List of high priority open issues should be 1", 1, prIssues.get("HIGH").size());
		assertEquals("Size of List of Medium priority open issues should be 2", 2, prIssues.get("MEDIUM").size());
		assertEquals("Size of List of low priority open issues should be 3", 3, prIssues.get("LOW").size());
	}
	
	@Test
	public void testGetOpenIssueIdGroupedbyLocation() {
		Map<String, List<String>> issuesByLocation = service.getOpenIssueIdGroupedbyLocation();
		assertNotNull("Map of open issues by location should not be null",issuesByLocation);
		assertEquals("Number of locations with open issues should be 5", 5, issuesByLocation.size());
		assertEquals("Number of open issues in Mysore should be 2", 2, issuesByLocation.get("Mysore").size());
		assertEquals("Number of open issues in Mandya should be 1", 1, issuesByLocation.get("Mandya").size());
	}
	
	@Test
	public void testGetHighMediumOpenIssueDuration() {
		Map<String, Long> issueDuration = service.getHighMediumOpenIssueDuration();
		assertNotNull("Map of high/medium priority issue cannot be null",issueDuration);
		assertEquals("Number of high/medium open issues should be 3", 3, issueDuration.size());
		assertEquals("Number of days between created date and current date is incorrect", Long.valueOf(30), issueDuration.get("ISS03"));
	}
}