package com.ebaykorea.monitoring.enumeration;

public enum ErrorLogType {
	NONE(0,"None"), INTERNAL(1,"INTERNAL"), CYCLE(2,"CYCLE");
	
    final private int num;
    final private String name;
    
    private ErrorLogType(int num, String name) { 
        this.name = name;
        this.num = num;
    }
    public final String getName() { 
        return name;
    }
    public final int getNum() {
    	return num;
    }
}
