/*
 * This class needs some refinement.
 * Name changes and method clean up.
 */
package com.uas.db;

import java.util.ArrayList;

public class QueryBuilder {
	
	public static class Operator{
		public static final String EQUALS = " = ";
		public static final String NOT_EQUALS = " != ";
		public static final String LIKE = " LIKE ";
		public static final String NOT_LIKE = " NOT LIKE ";
		public static final int MATCH_BEFORE = 0;
		public static final int MATCH_AFTER = 1;
		public static final int MATCH_ALL = 2;
		
		public static String charList(Object...expr){
			int wildCard = -1;
			String newExpr = null;
			for(int i = 0; i < expr.length; i = i + 2){
				if(expr[i + 1] instanceof Integer)
					wildCard = (int) expr[i + 1];
				if(wildCard == 0)
					newExpr = "'[" + expr[i] + "]%'";
				if(wildCard == 1)
					newExpr = "'%[" + expr[i] + "]'";
				if(wildCard == 2)
					newExpr = "'%[" + expr[i] + "]%'";
			}
			return newExpr;
		}
		
		public static String charList(String expr){
			return "'" + expr + "'";
		}
	}
	
	protected boolean distinct = false;
	protected boolean count = false;
	protected boolean distinctCount = false;
	
	protected ArrayList<Object> columns = new ArrayList<Object>();
	protected ArrayList<Object> tables = new ArrayList<Object>();
	protected ArrayList<Object> joins = new ArrayList<Object>();
	protected ArrayList<Object> leftJoins = new ArrayList<Object>();
	protected ArrayList<Object> wheres = new ArrayList<Object>();
	protected ArrayList<Object> groupBys = new ArrayList<Object>();
	protected ArrayList<Object> havings = new ArrayList<Object>();
	protected ArrayList<Object> orderBys = new ArrayList<Object>();
	protected ArrayList<Object> values = new ArrayList<Object>();
	protected ArrayList<Object> ons = new ArrayList<Object>();

	public QueryBuilder() {}
	
	public QueryBuilder from(Object table){
		tables.add(table);
		return this;
	}

	public QueryBuilder where(Object...expr){
		int wildCardInt = -1;
		String wildCardStr = "";
		int len = expr.length / 2;
		if(len % 2 == 0){
			for(int i = 0; i < expr.length; i = i + 4){
				if(expr[i + 3] instanceof Integer)
					wildCardInt = (int) expr[i + 3];
				if(expr[i + 3] instanceof String)
					wildCardStr = (String) expr[i + 3];
				if(wildCardInt != -1){
					if(expr[i + 1] instanceof String)
						if(wildCardInt == 0)
							wheres.add(expr[i] + "" + expr[i + 2] + "'"+expr[i + 1]+"%'");
						else if(wildCardInt == 1)
							wheres.add(expr[i] + "" + expr[i + 2] + "'%"+expr[i + 1]+"'");
						else if(wildCardInt == 2)
							wheres.add(expr[i] + "" + expr[i + 2] + "'%"+expr[i + 1]+"%'");
					if(expr[i + 1] instanceof Integer)
						if(wildCardInt == 0)
							wheres.add(expr[i] + "" + expr[i + 2] + expr[i + 1] + "%");
						else if(wildCardInt == 1)
							wheres.add(expr[i] + "" + expr[i + 2] + "%" + expr[i + 1]);
						else if(wildCardInt == 2)
							wheres.add(expr[i] + "" + expr[i + 2] + "%" + expr[i + 1] + "%");
				}
				if(wildCardStr != "")
					wheres.add(expr[i] + "" + expr[i + 2] + expr[i + 2]);
			}
		}
		else{
			for(int i = 0; i < expr.length; i = i + 3){
				if(expr[i + 1] instanceof String)
					wheres.add(expr[i] + "" + expr[i + 2] + "'"+expr[i + 1]+"'");
				if(expr[i + 1] instanceof Integer)
					wheres.add(expr[i] + "" + expr[i + 2] + expr[i + 1]);
			}
		}
		return this;
	}

	/*
	 * Select
	 */
	public class Select extends QueryBuilder{
		
		public Select() {}
		
		public Select(Object table){
			tables.add(table);
		}
		
		public Select distinct(Object col){
			distinct = true;
			columns.add(col);
			return this;
		}
		
		public Select count(Object col){
			count = true;
			columns.add(col);
			return this;
		}
		
		public Select distinctCount(Object col){
			distinctCount = true;
			columns.add(col);
			return this;
		}
		
		public Select column(Object...name){
			for(Object obj : name){
				columns.add(obj);
			}
			return this;
		}
		
		public Select column(Object name){
			columns.add(name);
			return this;
		}
		
	    public Select column(Object name, boolean groupBy) {
	        columns.add(name);
	        if (groupBy) {
	            groupBys.add(name);
	        }
	        return this;
	    }
	    
	    public Select on(Object expr1, Object expr2){
	    	ons.add(expr1 + " = " + expr2);
	    	return this;
	    }
	    
		public Select from(Object table){
			super.from(table);
			return this;
		}
		
		public Select join(Object expr){
			joins.add(expr);
			return this;
		}
		
		public Select leftJoin(Object...expr){
			for(int i = 0; i < expr.length; i = i + 3){
				leftJoins.add(expr[i] + " on "+ expr[i + 1] + " = " + expr[i + 2]);
			}
			return this;
		}
		
		public Select leftJoin(Object expr){
			leftJoins.add(expr);
			return this;
		}
		
		public Select groupBy(Object expr){
			groupBys.add(expr);
			return this;
		}
		
		public Select having(Object expr){
			havings.add(expr);
			return this;
		}
		
		public Select orderBy(Object expr){
			orderBys.add(expr);
			return this;
		}
		
		public Select where(Object...expr){
			super.where(expr);
			return this;
		}
		
		@Override
		public String toString(){
			StringBuilder sql = new StringBuilder("select ");
			
			if(distinct)
				sql.append("distinct ");
			if(count)
				sql.append("count");
			if(distinctCount)
				sql.append("count");
			if(columns.size() == 0){
				sql.append("*");
			}
			else{
				if(count)
					appendList(sql, columns, "(", ", ", ")");
				else if(distinctCount)
					appendList(sql, columns, "(distinct ", ", ", ")");
				else
					appendList(sql, columns, "", ", ");
			}

			appendList(sql, tables, " from ", ", ");
	        appendList(sql, joins, " join ", " join ");
	        appendList(sql, leftJoins, " left join ", " left join ");
	        appendList(sql, ons, " on ", " ");
			appendList(sql, wheres, " where ", " and ");
			appendList(sql, groupBys, " group by ", ", ");
			appendList(sql, havings, " having ", " and ");
			appendList(sql, orderBys, " order by ", ", ");
			
			return sql.toString();
		}
	}
	
	/*
	 * Insert
	 */
	public class Insert extends QueryBuilder {
		
		public Insert(){}
		
		public Insert(Object table) {
			tables.add(table);
		}
		
		public Insert into(Object table){
			tables.add(table);
			return this;
		}
		
		public Insert column(Object...expr){
			for(Object  obj : expr){
				columns.add(obj);
			}	
			return this;
		}
		
		public Insert value(Object...expr){
			for(Object  obj : expr){
				if(obj instanceof String)
					values.add("'"+obj+"'");
				if(obj instanceof Integer)
					values.add(obj);
			}	
			return this;
		}
		
		@Override
		public String toString(){
			StringBuilder sql = new StringBuilder("insert into ");
			
			appendList(sql, tables, " ", " ");
			appendList(sql, columns, " ( ", ", ", " )");
			appendList(sql, values, " values ( ", ", ", " ) ");
			
			return sql.toString();
		}
	}
	
	/*
	 * Update
	 */
	public class Update extends QueryBuilder {
		
		public Update(){}
		
		public Update(Object table) {
			tables.add(table);
		}
		
		public Update table(Object table){
			tables.add(table);
			return this;
		}
		
		public Update column(Object...expr){
			for(Object obj : expr){
				columns.add(obj);
			}	
			return this;
		}
		
		public Update value(Object...expr){
			for(int i = 0; i < expr.length; i ++){
				if(expr[i] instanceof String)
					columns.set(i, columns.get(i) + " = " + "'"+expr[i]+"'");
				if(expr[i] instanceof Integer)
					columns.set(i, columns.get(i) + " = " + expr[i]);
			}
			return this;
		}
		
		public Update set(Object...expr){
			for(int i = 0; i < expr.length; i = i + 2){
				if(expr[i + 1] instanceof String)
					columns.add(expr[i] + " = " + "'"+expr[i + 1]+"'");
				if(expr[i + 1] instanceof Integer)
					columns.add(expr[i] + " = " + expr[i + 1]);
			}
			return this;
		}
		
		public Update where(Object...expr){
			super.where(expr);
			return this;
		}

		@Override
		public String toString(){
			StringBuilder sql = new StringBuilder("update ignore ");
			
			appendList(sql, tables, "", " ");
			appendList(sql, columns, " set ", ", ");
			appendList(sql, wheres, " where ", " and ");
			
			return sql.toString();
		}
	}
	
	/*
	 * Delete
	 */
	public class Delete extends QueryBuilder{
		
		public Delete() {}
		
		public Delete(Object table) {
			tables.add(table);
		}

		public Delete from(Object table){
			super.from(table);
			return this;
		}
		
		public Delete where(Object...expr){
			super.where(expr);
			return this;
		}
		
		@Override
		public String toString(){
			StringBuilder sql = new StringBuilder("delete ");
			
			appendList(sql, tables, " from ", ", ");
			appendList(sql, wheres, " where ", " and ");
			
			return sql.toString();
		}
	}
	
	protected void appendList(StringBuilder sql, ArrayList<Object> list, String start, String sep){
		boolean first = true;
		for(Object obj : list){
			if(first)
				sql.append(start);
			else
				sql.append(sep);
			sql.append(obj);
			first = false;
		}
	}
	
	protected void appendList(StringBuilder sql, ArrayList<Object> list, String start, String sep, String end){
		boolean first = true;
		for(Object obj : list){
			if(first)
				sql.append(start);
			else
				sql.append(sep);
			sql.append(obj);	
			first = false;
		}
		sql.append(end);
	}
	
	public Select select(){
		return new Select();
	}
	
	public Insert insert(){
		return new Insert();
	}
	
	public Update update(){
		return new Update();
	}
	
	public Delete delete(){
		return new Delete();
	}
	
	public Insert insert(Object table){
		return new Insert(table);
	}
	
	public Update update(Object table){
		return new Update(table);
	}
	
	public Select select(Object table){
		return new Select(table);
	}
	
	public Delete delete(Object table){
		return new Delete(table);
	}
}

