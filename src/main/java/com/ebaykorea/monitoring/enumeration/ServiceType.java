package com.ebaykorea.monitoring.enumeration;

public enum ServiceType {
	NONE(0,"None"), GMARKET(1,"GMARKET"), AUCTION(2,"AUCTION"), COMMON(3,"COMMON");
	
    final private int num;
    final private String name;
    
    private ServiceType(int num, String name) { 
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
