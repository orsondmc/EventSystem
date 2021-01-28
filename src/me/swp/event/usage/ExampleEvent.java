package me.swp.event.usage;

import me.swp.event.Cancellable;
import me.swp.event.SWPEvent;

public class ExampleEvent extends SWPEvent implements Cancellable {
    public boolean cancelled = false;
    private String text;
    private int number;

    public ExampleEvent(int number, String text) {
        this.number = number;
        this.text = text;
    }

    public boolean isEventCancelled() {
        return cancelled;
    }

    public void cancelEvent(boolean cancel) {
        this.cancelled = cancel;
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }
}
