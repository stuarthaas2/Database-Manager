package com.uas.db.entity;

public interface IEntityTable {

	public INotifer notifer();
	
	public interface INotifer{
		
		public INotifer bind(String key, Object obj);
		
		public void unbind(String key);
		
		public INotifer set(String key, Object val);
		
		public INotifer update(String key, Object val);
		
		public INotifer clear(String key);
	}
}
