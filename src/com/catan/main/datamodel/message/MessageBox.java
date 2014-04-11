package com.catan.main.datamodel.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageBox implements Serializable {

    List<MessageLine> lines;

    public MessageBox() {
        this.lines = new ArrayList();
    }

    public List<MessageLine> getLines() {
        return this.lines;
    }
    public void setLines(List<MessageLine> lines) {
        this.lines = lines;
    }
    public void addLine(MessageLine line) {
        this.lines.add(line);
        if (this.lines.size() > 30) {
            this.lines.remove(0);
        }
    }

    //region Overrides
    @Override
    public String toString() {
        return "MessageBox{" +
                "lines=" + lines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageBox)) return false;

        MessageBox that = (MessageBox) o;

        if (lines != null ? !lines.equals(that.lines) : that.lines != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return lines != null ? lines.hashCode() : 0;
    }
    //endregion
}
