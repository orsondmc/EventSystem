package me.swp.event;

/**
 * @author github.com/SWP360
 * @since 28/1/2021
 */

public interface Cancellable {
    public boolean isEventCancelled();
    public void cancelEvent(boolean cancel);
}