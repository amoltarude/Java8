package com.stackroute.datamunger.reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;

public class CsvQueryProcessor extends QueryProcessingEngine {

	public Stream<String> lines;
	List<String> collect;
	Header head;
	public Stream<String> getLines() {
		return lines;
	}

	public void setLines(Stream<String> lines) {
		this.lines = lines;
	}

	public List<String> getCollect() {
		return collect;
	}

	public void setCollect(List<String> collect) {
		this.collect = collect;
	}

	public Header getHead() {
		return head;
	}

	public void setHead(Header head) {
		this.head = head;
	}

	public DataTypeDefinitions getDt() {
		return dt;
	}

	public void setDt(DataTypeDefinitions dt) {
		this.dt = dt;
	}

	DataTypeDefinitions dt;
	/*
	 * Parameterized constructor to initialize filename. As you are trying to
	 * perform file reading, hence you need to be ready to handle the IO
	 * Exceptions.
	 */

	public CsvQueryProcessor(String fileName) throws FileNotFoundException {
		head = new Header();
		dt = new DataTypeDefinitions();
		Path filePath = Paths.get(fileName);
		try {
			setLines(Files.newBufferedReader(filePath).lines());
			setCollect(lines.collect(Collectors.toList()));
			;
		} catch (IOException e) {
			throw new FileNotFoundException();
		}
	}

	/*
	 * Implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
	 */

	@Override
	public Header getHeader() throws IOException {

		try {
			String string = collect.get(0);
			head.setHeaders(string.split(","));
		} catch (Exception e) {

		}
		return head;
	}

	/**
	 * This method will be used in the upcoming assignments
	 */
	@Override
	public void getDataRow() {

	}

	/*
	 * Implementation of getColumnType() method. To find out the data types, we
	 * will read the first line from the file and extract the field values from
	 * it. In the previous assignment, we have tried to convert a specific field
	 * value to Integer or Double. However, in this assignment, we are going to
	 * use Regular Expression to find the appropriate data type of a field.
	 * Integers: should contain only digits without decimal point Double: should
	 * contain digits as well as decimal point Date: Dates can be written in
	 * many formats in the CSV file. However, in this assignment,we will test
	 * for the following date formats('dd/mm/yyyy',
	 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','
	 * yyyy-mm -dd')
	 */

	@Override
	public DataTypeDefinitions getColumnType() throws IOException {
		String[] columnType = head.getHeaders();
		String[] data = collect.get(1).concat(" ").split(",");
		String [] dataInfo;
		String s="";
		int j=0;
		List<String> lst = new ArrayList<String>();
		for (int i = 0; i < data.length; i++) {
			
			if (data[i].contains("\"")) {
				s=String.join(",", data[i+1]);
				i++;
			}else{
				s=data[i];
			}
			
			switch (columnType[j]) {
			case "id":
			case "season":
			case "dl_applied":
			case "win_by_runs":
			case "win_by_wickets":
				Integer temp = Integer.parseInt(s);
				lst.add(temp.getClass().getName());
				break;
			case "city":
			case "team1":
			case "team2":
			case "toss_winner":	
			case "toss_decision":
			case "result":
			case "winner":
			case "player_of_match":
			case "venue":
			case "umpire1":
			case "umpire2":
				s = data[i];
				lst.add(s.getClass().getName());
				break;
			case "date":
				try {
					ZoneId defaultZoneId = ZoneId.systemDefault();
					LocalDate parsedDate = LocalDate.parse(s, 
							DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]"));	
					Date date = Date.from(parsedDate.atStartOfDay(defaultZoneId).toInstant());
					lst.add(date.getClass().getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
			case "umpire3":
				Object temp1 =new Object();
				lst.add(temp1.getClass().getName());
				break;	
			
		
			default:
				break;
			}
			j++;
		}
		
		dataInfo = lst.stream().toArray(String[]::new);
		System.out.println(dataInfo.length);
					dt.setDataTypes(dataInfo);
		

		return dt;
	}

}
