package me.swp.event;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author github.com/SWP360
 * @since 28/1/2021
 */

public class EventBus {
    public List<Object> listeners;

    /**
     * Creates a new EventBus
     */
    public EventBus() {
        listeners = new ArrayList<>();
    }

    /**
     * Registers a class to receiving events.
     */
    public void register(Object clazz) {
        listeners.add(clazz);
    }

    /**
     * Unregisters a class from receiving events.
     */
    public void unregister(Object clazz) {
        listeners.remove(clazz);
    }

    /**
     * Dispatches an event.
     * First time adding code to a javadoc, sorry if it looks bad.
     * <pre>
     * {@code
     * TickEvent tickEvent = new TickEvent(60);
     * EVENT_BUS.dispatch(tickEvent);
     * }
     * @param arg The event to dispatch
     * </pre>
     */
    public void dispatch(SWPEvent arg) {
        // Loop over the methods ("functions")
        listeners.forEach(listener -> Arrays.stream(listener.getClass().getDeclaredMethods()).forEach(method -> {
            // Make sure function has arguments to accept
            if (method.getParameterCount() > 0) {
                // Make sure it has @SWPHandler
                if (method.isAnnotationPresent(SWPHandler.class)) {
                    try {
                        // Invokes the function with the event
                        method.invoke(listener, arg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }
}
