package com.ditagis.hcm.docsotanhoa.conectDB;

public abstract class AbstractDB {
	protected ConnectionDB condb;
	public AbstractDB(){
		this.condb = new ConnectionDB();
		}
}
	