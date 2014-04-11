package com.catan.main.datamodel.message;

import java.io.Serializable;

public class MessageLine implements Serializable {

    String source;
    String message;

    public MessageLine(String source, String message) {
        this.source = source;
        this.message = message;
    }

    public String getSource() {
        return this.source;
    }
    public String getMessage() {
        return this.message;
    }

    //region Overrides
    @Override
    public String toString() {
        return "MessageLine{" +
                "source='" + source + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageLine)) return false;

        MessageLine that = (MessageLine) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
    //endregion
}
