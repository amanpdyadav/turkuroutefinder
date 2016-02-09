package com.turku.historydatabase;

public  class Address {
    int _id;
    String _name;
    
     public Address(){}

    public Address(int id, String name){
        this._id = id;
        this._name = name;
    }
 
    public Address(String name){
        this._name = name;
    }
    
	public int get_id() {
		return _id;
	}
	
	public void set_id(int _id) {
		this._id = _id;
	}
	
	public String get_name() {
		return _name;
	}
	
	public void set_name(String _name) {
		this._name = _name;
	}
	
	public String toString() {
		return "address [name=" + _name + "]";
	}
}
