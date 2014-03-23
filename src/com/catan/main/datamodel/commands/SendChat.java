package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.message.MessageLine;

public class SendChat extends Command {

    //region Fields
    public String content;
    //endregion

    public SendChat() {
    }

    //region Overrides
    @Override
    public void action(DataModel model) {
        String source = model.getPlayerName(this.getPlayerIndex());
        System.out.println(source + " sent the message : '" + this.content + "'");
        model.getChat().addLine(new MessageLine(source, this.content));
    }

    @Override
    protected MessageLine getLog(DataModel model) {
        return null;
    }

    @Override
    public String toString() {
        return "SendChatLogic{" +
                "content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SendChat)) return false;

        SendChat sendChat = (SendChat) o;

        if (content != null ? !content.equals(sendChat.content) : sendChat.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
    //endregion
}
