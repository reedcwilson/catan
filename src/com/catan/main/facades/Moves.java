package com.catan.main.facades;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.commands.SendChat;

public class Moves {

    //region Fields
    private SendChat _sendChat;
    //endregion
	public DataModel sendChat(String params) {
        //SendChatParams sendChatParams = (SendChatParams)GenerateParams(params);
        _sendChat = new SendChat();
        _sendChat.doExecute(new DataModel());
		return new DataModel();
	}

	public DataModel rollNumber() {
		return new DataModel();
	}
	
	public DataModel finishTurn() {
		return new DataModel();
	}
	
	public DataModel buyDevCard() {
		return new DataModel();
	}
	
	public DataModel useYearOfPlenty() {
		return new DataModel();
	}
	
	public DataModel useRoadBuilding() {
		return new DataModel();
	}
	
	public DataModel useSoldier() {
		return new DataModel();
	}
	
	public DataModel useMonopoly() {
		return new DataModel();
	}
	
	public DataModel useMonument() {
		return new DataModel();
	}
	
	public DataModel buildRoad() {
		return new DataModel();
	}
	
	public DataModel buildSettlement() {
		return new DataModel();
	}
	
	public DataModel buildCity() {
		return new DataModel();
	}
	
	public DataModel offerTrade() {
		return new DataModel();
	}
	
	public DataModel acceptTrade() {
		return new DataModel();
	}
	
	public DataModel discard() {
		return new DataModel();
	}
}
