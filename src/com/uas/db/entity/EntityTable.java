package com.uas.db.entity;

import java.awt.Component;
import java.awt.Container;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.uas.db.Database;
import com.uas.utils.Console;
import com.uas.utils.KeyObject;

public abstract class EntityTable implements IEntityTable{

	protected Database database;
	protected PreparedStatement pst = null;
	protected ResultSet rs = null;
	protected ResultSetMetaData rsmd = null;
	protected String sql;
	protected HashMap<String, Object> observers = new HashMap<String, Object>();
	protected ArrayList<KeyObject> keyList = new ArrayList<KeyObject>();
	
	public EntityTable(Database database){
		this.database = database;
	}
	
	public String getSql(){
		return sql;
	}
	
	public INotifer notifer(){
		return new Notifier();
	}
	
	/*
	 * Unused so far
	 * Will be used to bind UI
	 * properties to data 
	 * received from the database
	 */
	class Notifier implements INotifer, PropertyChangeListener, ItemListener{
		
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			Console.println("New Value: " + e.getNewValue());
			Console.println("Old Value: " + e.getOldValue());
		}
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.DESELECTED){
				Console.println("Old Value: " + e.getItem().toString());
			}
			if(e.getStateChange() == ItemEvent.SELECTED){
				Console.println("New Value: " + e.getItem().toString());
			}
		}
	
		@Override
		public INotifer bind(String key, Object obj){
			observers.put(key, obj);
			((Container) obj).addPropertyChangeListener(this);
			((ItemSelectable) obj).addItemListener(this);
			return this;
		}
		
		@Override
		public void unbind(String key) {
			((Component) observers.get(key)).removePropertyChangeListener(this);
			((ItemSelectable) observers.get(key)).removeItemListener(this);
			observers.remove(key);
		}
		
		@Override
		public INotifer set(String key, Object val){
			Object obj = observers.get(key);
			if(obj instanceof JTextField)
				((JTextField) obj).setText((String) val);
			if(obj instanceof JComboBox)
				((JComboBox<?>) obj).setSelectedItem((String) val);
			return this;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public INotifer update(String key, Object val) {
			Object obj = observers.get(key);
			if(obj instanceof JTextField)
				((JTextField) obj).setText((String) val);
			if(obj instanceof JComboBox)
				if(val instanceof ArrayList)
					for(Object v : (ArrayList<?>)val)
						((JComboBox<Object>) obj).addItem(v);
				else
					((JComboBox<Object>) obj).addItem(val);
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public INotifer clear(String key) {
			Object obj = observers.get(key);
			if(obj instanceof JTextField)
				((JTextField) obj).setText("");
			if(obj instanceof JComboBox)
				((JComboBox<Object>) obj).removeAllItems();
			return this;
		}
	}
}
