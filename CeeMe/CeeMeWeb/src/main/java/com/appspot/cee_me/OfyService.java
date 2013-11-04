package com.appspot.cee_me;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import com.appspot.cee_me.model.*;

public class OfyService {
    static {
    	// Perform entity registrations here
        factory().register(CUser.class);
        factory().register(Media.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}