package com.learn.issuetracker.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.learn.issuetracker.model.Employee;
import com.learn.issuetracker.model.Issue;

/*
 * This class is used to read the issues data from the file and store it in a collection. Java8 NIO 
 * should be used to read the file in to streams
*/
public class IssueRepositoryImpl implements IssueRepository {

	/*
	 * This List will store the issue details read from the file
	 */
	private List<Issue> issues;

	/*
	 * issuesFilePath variable is used to store the path of issues.csv file
	 */
	private Path issuesFilePath;

	public Path getIssuesFilePath() {
		return issuesFilePath;
	}

	public void setIssuesFilePath(Path issuesFilePath) {
		this.issuesFilePath = issuesFilePath;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	/*
	 * Initialize the member variables in the parameterized constructor
	 * initializeIssuesFromFile() method should be used in the constructor to
	 * initialize the 'issues' instance variable
	 *
	 */
	public IssueRepositoryImpl(Path issuesFilePath) {
		setIssuesFilePath(issuesFilePath);
		try {
			setIssues(Files.newBufferedReader(issuesFilePath).lines().map(p-> parseIssue(p)).collect(Collectors.toList()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method should read the file from the path stored in variable
	 * 'issuesFilePath'. It should store the records read from the file in a List
	 * and initialize the 'issues' variable with this list. It should use
	 * 'parseIssue' method of Utility class for converting the line read from the
	 * file in to an Issue Object. Any issue with ISSUE ID, not starting with "IS",
	 * should not be stored in the "issues" List.
	 */

	public void initializeIssuesFromFile() {
		try {
			List<Issue> collect = Files.newBufferedReader(getIssuesFilePath()).lines().map(p-> parseIssue(p)).collect(Collectors.toList());
			setIssues(Files.newBufferedReader(getIssuesFilePath()).lines().map(Utility::parseIssue)
					.filter(p->!p.getIssueId().startsWith("IS")).collect(Collectors.toList()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Getter Method
	 */
	public List<Issue> getIssues() {
		return issues;
	}
	
	public Issue parseIssue(String line) {
	
			String[] data = line.split(",");
			
			return new Issue(data[0], data[1], LocalDate.parse(data[2], DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]")), 
					LocalDate.parse(data[3], DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]")), data[4],
					data[5], EmployeeRepository.getEmployee(Integer.parseInt(data[6])).orElse(new Employee()));
	
	}
}
