package com.dans.domain

class CD {

	String artist
	String name
	Integer year

    static constraints = {
    	    artist size: 0..20, blank: false
    	    name size: 0..20, blank: false
    	    year min: 1990
    }
}
