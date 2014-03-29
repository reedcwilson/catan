package com.catan.main.datamodel.player;

import com.google.inject.*;

public class TurnTrackerModule extends AbstractModule{
    @Override
    protected void configure(){
        bind(TurnTrackerInterface.class).to(TurnTracker.class);
    }

}
