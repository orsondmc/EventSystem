package me.swp.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

/**
 * @author github.com/SWP360
 * @since 28/1/2021
 */

public class EventBus {
    public HashMap<Object, ArrayList<Method>> listeners;

    /**
     * Creates a new EventBus
     */
    public EventBus() {
        listeners = new HashMap<>();
    }

    /**
     * Registers a class to receiving events.
     */
    public void register(Object clazz) {
        ArrayList<Method> results = new ArrayList<>();
        Arrays.stream(clazz.getClass().getDeclaredMethods()).forEach(method -> {
            if (method.getParameterCount() > 0) {
                if (method.isAnnotationPresent(SWPHandler.class)) {
                    results.add(method);
                }
            }
        });
        listeners.put(clazz, results);
    }

    /**
     * Unregisters a class from receiving events.
     */
    public void unregister(Object clazz) {
        try {
            listeners.remove(clazz);
        } catch (ConcurrentModificationException ignored) {
            throw new ConcurrentCopingException();
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
        listeners.forEach((key, value) -> value.forEach(method -> {
            if (method.getParameters()[0].getType().getName().equals(arg.getClass().getName())) {
                try {
                    method.invoke(key, arg);
                } catch (ConcurrentModificationException | IllegalAccessException | InvocationTargetException ignored) {
                    throw new ConcurrentCopingException();
                }
            }
        }));
    }
}
