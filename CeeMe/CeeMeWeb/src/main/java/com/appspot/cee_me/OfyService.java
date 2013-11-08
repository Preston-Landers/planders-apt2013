package com.appspot.cee_me;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

import com.appspot.cee_me.model.*;

public class OfyService {
    static {

        // Lets use use Joda DateTime as entity properties
        JodaTimeTranslators.add(factory());

    	// Perform entity registrations here
        factory().register(CUser.class);
        factory().register(Media.class);
        factory().register(Device.class);
        factory().register(Message.class);


    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}