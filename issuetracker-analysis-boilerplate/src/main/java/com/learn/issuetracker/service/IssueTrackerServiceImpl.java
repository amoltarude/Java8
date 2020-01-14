package com.learn.issuetracker.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.learn.issuetracker.exceptions.IssueNotFoundException;
import com.learn.issuetracker.model.Employee;
import com.learn.issuetracker.model.Issue;
import com.learn.issuetracker.repository.IssueRepository;

/*
 * This class contains functionalities for searching and analyzing Issues data Which is stored in a collection
 * Use JAVA8 STREAMS API to do the analysis
 * 
*/
public class IssueTrackerServiceImpl implements IssueTrackerService {

	/*
	 * CURRENT_DATE contains the date which is considered as todays date for this
	 * application Any logic which uses current date in this application, should
	 * consider this date as current date
	 */
	private static final String CURRENT_DATE = "2019-05-01";

	/*
	 * The issueDao should be used to get the List of Issues, populated from the
	 * file
	 */
	private IssueRepository issueDao;
	private LocalDate today;

	public IssueRepository getIssueDao() {
		return issueDao;
	}

	public void setIssueDao(IssueRepository issueDao) {
		this.issueDao = issueDao;
	}

	public LocalDate getToday() {
		return today;
	}

	public void setToday(LocalDate today) {
		this.today = today;
	}

	/*
	 * Initialize the member variables Variable today should be initialized with the
	 * value in CURRENT_DATE variable
	 */
	public IssueTrackerServiceImpl(IssueRepository issueDao) {
		setIssueDao(issueDao);
		setToday(LocalDate.now());
	}

	/*
	 * In all the below methods, the list of issues should be obtained by used
	 * appropriate getter method of issueDao.
	 */
	/*
	 * The below method should return the count of issues which are closed.
	 */
	@Override
	public long getClosedIssueCount() {
		long count = getIssueDao().getIssues().stream().filter(p-> p.getStatus().equals("CLOSED")).count();
		return count;
	}

	/*
	 * The below method should return the Issue details given a issueId. If the
	 * issue is not found, method should throw IssueNotFoundException
	 */

	@Override
	public Issue getIssueById(String issueId) throws IssueNotFoundException {
		Issue issue = getIssueDao().getIssues().stream().filter(p-> p.getIssueId().equals(issueId)).findFirst().get();
		if (issue!=null) {
			return issue;
		}else {
			throw new IssueNotFoundException();
		}
	}

	/*
	 * The below method should return the Employee Assigned to the issue given a
	 * issueId. It should return the employee in an Optional. If the issue is not
	 * assigned to any employee or the issue Id is incorrect the method should
	 * return empty optional
	 */
	@Override
	public Optional<Employee> getIssueAssignedTo(String issueId) {
		Optional<Employee> findFirst = getIssueDao().getIssues().stream().filter(p->p.getIssueId().equals(issueId)).map(p->p.getAssignedTo()).findFirst();
		return findFirst;
	}

	/*
	 * The below method should return the list of Issues given the status. The
	 * status can contain values OPEN / CLOSED
	 */
	@Override
	public List<Issue> getIssuesByStatus(String status) {
		List<Issue> collect = getIssueDao().getIssues().stream().filter(p->p.getStatus().equals(status)).collect(Collectors.toList());
		return collect;
	}

	/*
	 * The below method should return a LinkedHashSet containing issueid's of open
	 * issues in the ascending order of expected resolution date
	 */
	@Override
	public Set<String> getOpenIssuesInExpectedResolutionOrder() {
		HashSet<String> collect = getIssueDao().getIssues().stream().filter(p->p.getStatus().equals("OPEN"))
				.sorted(Comparator.comparing(Issue::getExpectedResolutionOn))
		.map(p->p.getIssueId()).collect(Collectors.toCollection(LinkedHashSet::new));
		return collect;
	}

	/*
	 * The below method should return a List of open Issues in the descending order
	 * of Priority and ascending order of expected resolution date within a priority
	 */
	@Override
	public List<Issue> getOpenIssuesOrderedByPriorityAndResolutionDate() {
		List<Issue> collect = getIssueDao().getIssues().stream().filter(p->p.getStatus().equals("OPEN"))
		.sorted(Comparator.comparing(Issue::getPriority).reversed().thenComparing(Issue::getExpectedResolutionOn)).collect(Collectors.toList());
		//.
		return collect;
	}

	/*
	 * The below method should return a List of 'unique' employee names who have
	 * issues not closed even after 7 days of Expected Resolution date. Consider the
	 * current date as 2019-05-01
	 */
	@Override
	public List<String> getOpenIssuesDelayedbyEmployees() {
		Map<String, Long> collect = getIssueDao().getIssues().stream().filter(p->p.getStatus().equals("OPEN"))
				.filter(p->p.getPriority().equals("HIGH") || p.getPriority().equals("MEDIUM"))
				.collect(Collectors.toMap(Issue::getIssueId, p->ChronoUnit.DAYS.between(p.getCreatedOn(), LocalDate.parse(CURRENT_DATE, DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]")))));
		List<String> collect2 = getIssueDao().getIssues().stream().filter(p->p.getStatus().equals("OPEN")).filter(p->ChronoUnit.DAYS.between(p.getExpectedResolutionOn(), LocalDate.parse(CURRENT_DATE, DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]")))>7)
		
				.map(p->p.getAssignedTo().getName()).distinct().collect(Collectors.toList());
		return collect2;
	}

	/*
	 * The below method should return a map with key as issueId and value as
	 * assigned employee name. THe Map should contain details of open issues having
	 * HIGH priority
	 */
	@Override
	public Map<String, Integer> getHighPriorityOpenIssueAssignedTo() {
		Map<String, Integer> collect = getIssueDao().getIssues().stream().filter(p-> p.getPriority().equals("HIGH") && p.getStatus().equals("OPEN"))
		.collect(Collectors.toMap(p->p.getIssueId(), p->p.getAssignedTo().getEmplId()));
		return collect;
	}

	/*
	 * The below method should return open issues grouped by priority in a map. The
	 * map should have key as issue priority and value as list of open Issues
	 */
	@Override
	public Map<String, List<Issue>> getOpenIssuesGroupedbyPriority() {
		Map<String, List<Issue>> collect = getIssueDao().getIssues().stream()
				.filter(p->p.getStatus().equals("OPEN")).collect(Collectors.groupingBy(p-> p.getPriority(), Collectors.toList()));
		return collect;
	}

	/*
	 * The below method should return count of open issues grouped by priority in a map. 
	 * The map should have key as issue priority and value as count of open issues 
	 */
	@Override
	public Map<String, Long> getOpenIssuesCountGroupedbyPriority() {
		Map<String, Long> collect = getIssueDao().getIssues().stream().filter(p->p.getStatus().equals("OPEN"))
		.collect(Collectors.groupingBy(p->p.getPriority(), Collectors.counting()));
		return collect;
	}
	
	/*
	 * The below method should provide List of issue id's(open), grouped by location
	 * of the assigned employee. It should return a map with key as location and
	 * value as List of issue Id's of open issues
	 */
	@Override
	public Map<String, List<String>> getOpenIssueIdGroupedbyLocation() {
		Map<String, List<String>> collect = getIssueDao().getIssues().stream().filter(p->p.getStatus().equalsIgnoreCase("OPEN"))
		.collect(Collectors.groupingBy(p->p.getAssignedTo().getLocation(),Collectors.mapping(p->p.getIssueId(), Collectors.toList())));
		return collect;
	}
	
	/*
	 * The below method should provide the number of days, since the issue has been
	 * created, for all high/medium priority open issues. It should return a map
	 * with issueId as key and number of days as value. Consider the current date as
	 * 2019-05-01
	 */
	@Override
	public Map<String, Long> getHighMediumOpenIssueDuration() {
		Map<String, Long> collect = getIssueDao().getIssues().stream().filter(p->p.getStatus().equals("OPEN")).filter(p->p.getPriority().equals("HIGH") || p.getPriority().equals("MEDIUM"))
		.collect(Collectors.toMap(Issue::getIssueId, p->ChronoUnit.DAYS.between(p.getCreatedOn(), LocalDate.parse(CURRENT_DATE, DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]")))));
		return collect;
	}
	
	
}