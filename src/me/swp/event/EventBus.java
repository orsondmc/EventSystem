package me.swp.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

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
        try {
            listeners.add(clazz);
        } catch (ConcurrentModificationException ignored) {
        }
    }

    /**
     * Unregisters a class from receiving events.
     */
    public void unregister(Object clazz) {
        try {
            listeners.remove(clazz);
        } catch (ConcurrentModificationException ignored) {
        }
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
        listeners.forEach(listener -> Arrays.stream(listener.getClass().getDeclaredMethods()).forEach(method -> {
            if (method.getParameterCount() > 0) {
                if (method.isAnnotationPresent(SWPHandler.class)) {
                    if (method.getParameters()[0].getType().getName().equals(arg.getClass().getName())) {
                        try {
                            method.invoke(listener, arg);
                        } catch (ConcurrentModificationException | IllegalAccessException | InvocationTargetException ignored) {
                        }
                    }
                }
            }
        }));
    }
}
