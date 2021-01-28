package me.swp.event;

public interface Cancellable {
    public boolean isEventCancelled();
    public void cancelEvent(boolean cancel);
}