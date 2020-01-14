package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryParser {
	private QueryParameter queryParameter = new QueryParameter();
	
	/*
	 * this method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		queryParameter.setRestrictions(getRestrictions(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));
		return queryParameter;
	}
	
		/*
		 * extract the name of the file from the query. File name can be found after the
		 * "from" clause.
		 */
	public String getFileName(String queryString) {
		   String filename = queryString.toLowerCase().split("from ")[1].split(" ")[0];
		return filename;
	}
		/*
		 * extract the order by fields from the query string. Please note that we will
		 * need to extract the field(s) after "order by" clause in the query, if at all
		 * the order by clause exists. For eg: select city,winner,team1,team2 from
		 * data/ipl.csv order by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one order by fields.
		 */
	public List<String> getOrderByFields(String queryString) {
		String temp = queryString.toLowerCase().contains("order by") ?queryString.toLowerCase().split(" order by ")[1].trim():null;
		if (temp!=null) {
			//String[] field = {temp};
			List<String> lst = new ArrayList<String>();
			lst.add(temp);
			return lst;
		} else {
			return null;
		}
	}
		
		/*
		 * extract the group by fields from the query string. Please note that we will
		 * need to extract the field(s) after "group by" clause in the query, if at all
		 * the group by clause exists. For eg: select city,max(win_by_runs) from
		 * data/ipl.csv group by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one group by fields.
		 */
	public List<String> getGroupByFields(String queryString) {
		String temp;
		List<String> lst = new ArrayList<String>();
		temp= queryString.toLowerCase().contains("group by") ? queryString.toLowerCase().split(" group by ")[1].trim():null;
		String res = temp!=null && temp.toLowerCase().contains("order by") ?temp.toLowerCase().split(" order by ")[0].trim():temp;

		//List<String> lst = new ArrayList<String>();
		lst.add(res);
		return lst;
	}
		
		/*
		 * extract the selected fields from the query string. Please note that we will
		 * need to extract the field(s) after "select" clause followed by a space from
		 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
		 * query mentioned above, we need to extract "city" and "win_by_runs". Please
		 * note that we might have a field containing name "from_date" or "from_hrs".
		 * Hence, consider this while parsing.
		 */
	public List<String> getFields(String queryString) {
		String strSelect = queryString.toLowerCase().split("select")[1].trim();
		String strFrom = strSelect.split("from")[0].trim();
		String[] selectFields =null;
		ArrayList<String> list = new ArrayList<String>();
		if(strFrom.contains(",")) {
			selectFields = strFrom.split(",");
		for(int i=0;i<selectFields.length;i++) {
			list.add(selectFields[i].trim());
		}
		return list;
		}else {
			list.add(strFrom);
			return list;
		}
	}	
		
		
		
		/*
		 * extract the conditions from the query string(if exists). for each condition,
		 * we need to capture the following: 
		 * 1. Name of field 
		 * 2. condition 
		 * 3. value
		 * 
		 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
		 * where season >= 2008 or toss_decision != bat
		 * 
		 * here, for the first condition, "season>=2008" we need to capture: 
		 * 1. Name of field: season 
		 * 2. condition: >= 
		 * 3. value: 2008
		 * 
		 * the query might contain multiple conditions separated by OR/AND operators.
		 * Please consider this while parsing the conditions.
		 * 
		 */
	public List<Restriction> getRestrictions(String queryString){
	    String inlower=queryString.trim();
        String tokens[]=inlower.trim().split("where");
        
        if(tokens.length==1)
            {return null ;}
        
        String conditions[]=tokens[1].trim().split("order by|group by");
        String indi[]=conditions[0].trim().split(" and | or ");
        List<Restriction>restrictionList=new LinkedList<Restriction>();
        for (String string : indi) {
            String condition = "";
            if(string.contains(">=")) {
                condition = ">=";
            } else if(string.contains("<=")) {
                condition = "<=";
            } else if(string.contains("!=")) {
                condition = "!=";
            } else if(string.contains(">")) {
                condition = ">";
            } else if(string.contains("<")) {
                condition = "<";
            } else if(string.contains("=")) {
                condition = "=";
            }
            String name = string.split(condition)[0].trim();
            String value = string.split(condition)[1].trim().replaceAll("'", "");
            Restriction res = new Restriction(); 
            res.setPropertyName(name);
            res.setCondition(condition);
            res.setPropertyValue(value);
            //Restriction restrictionInstance = new Restriction(name, value, condition);
            restrictionList.add(res);
        }
        return restrictionList;
	}
		
		/*
		 * extract the logical operators(AND/OR) from the query, if at all it is
		 * present. For eg: select city,winner,team1,team2,player_of_match from
		 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
		 * bangalore
		 * 
		 * the query mentioned above in the example should return a List of Strings
		 * containing [or,and]
		 */
	public List<String> getLogicalOperators(String queryString){
		List<String> logicalOperators = new ArrayList<>();
		String[] temp=queryString.split(",|\\s+");
		logicalOperators=Arrays.stream(temp).filter(p->p.equals("and") || p.equals("or")).collect(Collectors.toList());
		return logicalOperators.isEmpty()?null:logicalOperators;
	}	
		
		/*
		 * extract the aggregate functions from the query. The presence of the aggregate
		 * functions can determined if we have either "min" or "max" or "sum" or "count"
		 * or "avg" followed by opening braces"(" after "select" clause in the query
		 * string. in case it is present, then we will have to extract the same. For
		 * each aggregate functions, we need to know the following: 
		 * 1. type of aggregate function(min/max/count/sum/avg) 
		 * 2. field on which the aggregate function is being applied
		 * 
		 * Please note that more than one aggregate function can be present in a query
		 * 
		 * 
		 */
		//return null;
	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		List<AggregateFunction> aggregate = new ArrayList<>();
		String[]field=queryString.toLowerCase().split(",|\\s+");
		for (String str : field) {
			String[] temp;
			if(str.matches(".*sum.*")|| str.matches(".*count.*")||str.matches(".*max.*")
					||str.matches(".*min.*")||str.matches(".*avg.*")) {
				temp=str.split("[(|)]");
				aggregate.add(new AggregateFunction(temp[1].trim(), temp[0].trim()));
			}
			
		}
		return aggregate;
	}
	
	public String getBaseQuery(String queryString) {
		String[] filename = queryString.split("where | group by ");
		return filename[0].trim();
	}
	
}
