package com.learn.issuetracker.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.learn.issuetracker.model.Employee;

/*
 * This class is used to read employees data from the file and store the data in a List.
 * Java 8 NIO should be used to read the file data in to streams 
*/
public class EmployeeRepository {

	/*
	 * This List will store the employee details read from the file
	 */
	private static List<Employee> employees;

	public static Stream<String> lines;
	/*
	 * This static block should populate the 'employees' List by calling the static
	 * method 'initializeEmployeesFromFile' of this class. The path of the
	 * employees.csv file is "src --> data -> employees.csv"
	 */
	static {
		
	}

	/*
	 * This method is used to read from the file given in the input Path parameter.
	 * It should store all the records read from the file in to 'employees' member
	 * variable. This method should use 'parseEmployee' method of Utility class for
	 * converting the line read from the file in to Employee Object
	 */
	public static void initializeEmployeesFromFile(Path employeesfilePath) {
		try {
			setLines(Files.newBufferedReader(employeesfilePath).lines());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static Stream<String> getLines() {
		return lines;
	}



	public static void setLines(Stream<String> lines) {
		EmployeeRepository.lines = lines;
	}



	/*
	 * getEmployee method should search the 'employees' List based on the input
	 * employee Id, and return the employee found, in an Optional<Employee> object
	 */
	public static Optional<Employee> getEmployee(int empId) {
		Path path = Paths.get("src", "data", "employees.csv");
		try {
		setLines(Files.newBufferedReader(path).lines());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getLines().map(EmployeeRepository::parseEmployee).collect(Collectors.toList())
				.stream().filter(p->p.getEmplId()==empId).findAny();
	}

	// Getter
	public static List<Employee> getEmployees() {
		employees = getLines().map(EmployeeRepository::parseEmployee).collect(Collectors.toList());
		return employees;
	}
	
	public static Employee parseEmployee(String line) {
		String[] data = line.split(",");
		return new Employee(Integer.parseInt(data[0]), data[1], data[2]);
	}
}