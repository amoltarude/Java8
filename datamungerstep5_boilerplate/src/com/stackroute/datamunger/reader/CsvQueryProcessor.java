package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.stackroute.datamunger.query.DataSet;
import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Filter;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.query.Row;
import com.stackroute.datamunger.query.RowDataTypeDefinitions;
import com.stackroute.datamunger.query.parser.QueryParameter;




public class CsvQueryProcessor implements QueryProcessingEngine {
	/*
	 * This method will take QueryParameter object as a parameter which contains the
	 * parsed query and will process and populate the ResultSet
	 */
	public DataSet getResultSet(QueryParameter queryParameter) {
		DataSet dataSet=new DataSet();
		/*
		 * initialize BufferedReader to read from the file which is mentioned in
		 * QueryParameter. Consider Handling Exception related to file reading.
		 */
		try {
			BufferedReader reader=new BufferedReader(new FileReader(queryParameter.getFileName()));	
		/*
		 * read the first line which contains the header. Please note that the headers
		 * can contain spaces in between them. For eg: city, winner
		 */
			String[] header=reader.readLine().split(",");
			reader.mark(1);
		
		/*
		 * read the next line which contains the first row of data. We are reading this
		 * line so that we can determine the data types of all the fields. Please note
		 * that ipl.csv file contains null value in the last column. If you do not
		 * consider this while splitting, this might cause exceptions later
		 */
			String[] row=reader.readLine().split(",", header.length);
		/*
		 * populate the header Map object from the header array. header map is having
		 * data type <String,Integer> to contain the header and it's index.
		 */
			Header headerMap=new Header();
			for(int i=0;i<header.length;i++) {
				
				headerMap.put(header[i].trim(),i);
			}
		
		/*
		 * We have read the first line of text already and kept it in an array. Now, we
		 * can populate the RowDataTypeDefinition Map object. RowDataTypeDefinition map is
		 * having data type <Integer,String> to contain the index of the field and it's
		 * data type. To find the dataType by the field value, we will use getDataType()
		 * method of DataTypeDefinitions class
		 */
			RowDataTypeDefinitions rowDataTypeDefinitions=new RowDataTypeDefinitions();
			
			for(int iCnt=0;iCnt<row.length;iCnt++) {
				
				rowDataTypeDefinitions.put(iCnt,(String)DataTypeDefinitions.getDataType(row[iCnt]));
			}
		
		/*
		 * once we have the header and dataTypeDefinitions maps populated, we can start
		 * reading from the first line. We will read one line at a time, then check
		 * whether the field values satisfy the conditions mentioned in the query,if
		 * yes, then we will add it to the resultSet. Otherwise, we will continue to
		 * read the next line. We will continue this till we have read till the last
		 * line of the CSV file.
		 */
		
		
		/* reset the buffered reader so that it can start reading from the first line */
			reader.reset();
		
		/*
		 * skip the first line as it is already read earlier which contained the header
		 */
		
		
		/* read one line at a time from the CSV file till we have any lines left */
		
		
		/*
		 * once we have read one line, we will split it into a String Array. This array
		 * will continue all the fields of the row. Please note that fields might
		 * contain spaces in between. Also, few fields might be empty.
		 */
		
		
		/*
		 * if there are where condition(s) in the query, test the row fields against
		 * those conditions to check whether the selected row satifies the conditions
		 */
		
		
		/*
		 * from QueryParameter object, read one condition at a time and evaluate the
		 * same. For evaluating the conditions, we will use evaluateExpressions() method
		 * of Filter class. Please note that evaluation of expression will be done
		 * differently based on the data type of the field. In case the query is having
		 * multiple conditions, you need to evaluate the overall expression i.e. if we
		 * have OR operator between two conditions, then the row will be selected if any
		 * of the condition is satisfied. However, in case of AND operator, the row will
		 * be selected only if both of them are satisfied.
		 */
		
		
		/*
		 * check for multiple conditions in where clause for eg: where salary>20000 and
		 * city=Bangalore for eg: where salary>20000 or city=Bangalore and dept!=Sales
		 */
		
		
		/*if the overall condition expression evaluates to true, then we 
		 * need to check if all columns are to be selected(select *) or few
		 * columns are to be selected(select col1,col2).
		 * In either of the cases, we will have to populate the row map object.
		 * Row Map object is having type <String,String> to contain field Index
		 * and field value for the selected fields.
		 * Once the row object is populated, add it to DataSet Map Object.
		 * DataSet Map object is having type <Long,Row> to hold the rowId
		 * (to be manually generated by incrementing a Long variable) and
		 * it's corresponding Row Object. */
		
		
		/*return dataset object*/
			long setRowIndex=1;
			Filter filter=new Filter();
			String line;
			
			while((line=reader.readLine())!=null) {
				
				  String[] rowFileds=line.split(",", header.length);
				  Boolean result=false;
				  ArrayList<Boolean> flags=new ArrayList<Boolean>();
				  
				  if(queryParameter.getRestrictions()==null) {
					  result=true;
				  }
				  else {
					  
					  for(int i=0;i<queryParameter.getRestrictions().size();i++) {
						  
						  int index=headerMap.get(queryParameter.getRestrictions().get(i).getPropertyName());
						  flags.add(filter.evaluateExpression(queryParameter.getRestrictions().get(i),rowFileds[index].trim(),rowDataTypeDefinitions.get(index)));		  			  
						  result=solveOperators(flags, queryParameter.getLogicalOperators());
					  }
				  }
				  if(result) {
					  
					  Row rowMap=new Row();
					  for(int iCount=0;iCount<queryParameter.getFields().size();iCount++) {
						  if(queryParameter.getFields().get(iCount).equals("*")) {
							  for(int jCount=0;jCount<rowFileds.length;jCount++) {
								  rowMap.put(header[jCount].trim(), rowFileds[jCount]);
							  }
						  }
						  else {
							  rowMap.put(queryParameter.getFields().get(iCount), rowFileds[headerMap.get(queryParameter.getFields().get(iCount))]);
						  }
					  }
					  
					  dataSet.put(setRowIndex++, rowMap);
				  }
				
			}
			
			reader.close();
		}
		catch(FileNotFoundException e) {
			
			e.printStackTrace();
		}
		catch(IOException e) {
			
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return dataSet;
	}
	
	
	
private boolean solveOperators(ArrayList<Boolean> flags, List<String> operators) {
		
		if(flags.size()==1) {
			return flags.get(0);
		}
		else if(flags.size()==2) {
			if(operators.get(0).toLowerCase().matches("and")) {
				return flags.get(0)&flags.get(1);
			}
			else {
				return flags.get(0)|flags.get(1);
			}
		}
		else if(flags.size()==3) {
			
			int i=operators.indexOf("AND|and");
			
			if(i<0) {
				return flags.get(0)|flags.get(1)|flags.get(2);
			}
			else if(i==0) {
				return flags.get(0)&flags.get(1)|flags.get(2);
			}
			else if(i==1) {
				return flags.get(0)|flags.get(1)&flags.get(2);
			}
			else {
				return false;
			}
			
		}
		else
			return false;
	}
	
}
