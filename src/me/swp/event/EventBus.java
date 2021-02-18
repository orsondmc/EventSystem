package me.swp.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author github.com/SWP360
 * @since 28/1/2021
 */

public final class EventBus {

    private static final Logger LOGGER = Logger.getLogger(EventBus.class.getName());

    private final ConcurrentHashMap<Class<? extends SWPEvent>, List<ListenerInfo>> listeners = new ConcurrentHashMap<>();

    /**
     * Creates a new EventBus
     */
    public EventBus() {}

    /**
     * Registers a class to receiving events.
     */
    public void subscribe(Object listener) {
        Arrays.stream(listener.getClass().getDeclaredMethods())
            .filter(method -> method.getParameterCount() == 1
                    && method.isAnnotationPresent(SWPHandler.class)
                    && SWPEvent.class.isAssignableFrom(method.getParameters()[0].getType()))
            .forEach(method -> {
                Parameter parameter = method.getParameters()[0];
                listeners.compute((Class<? extends SWPEvent>)parameter.getType(), (swpEventClass, listenerInfos) -> {
                    listenerInfos = listenerInfos == null ? new ArrayList<>() : listenerInfos;
                    listenerInfos.add(new ListenerInfo(listener, method, swpEventClass));
                    return listenerInfos;
                });
            });
    }

    /**
     * Unregisters a class from receiving events.
     */
    public void unsubscribe(Object listener) {
        listeners.values().stream().flatMap(Collection::stream)
                .filter(listenerInfo -> listenerInfo.target.equals(listener))
                .forEach(listenerInfo -> {
                    listeners.compute(listenerInfo.eventType, (aClass, listenerInfos) -> {
                        if (listenerInfos != null) {
                            listenerInfos.remove(listenerInfo);
                        }
                        return listenerInfos;
                });
        });
    }

    /**
     * Dispatches an event.
     * First time adding code to a javadoc, sorry if it looks bad.
     * <pre>
     * {@code
     * TickEvent tickEvent = new TickEvent(60);
     * EVENT_BUS.dispatch(tickEvent);
     * }
     * @param event The event to dispatch
     * </pre>
     */
    public void dispatch(SWPEvent event) {
        List<ListenerInfo> listenerInfos = listeners.get(event.getClass());
        if (listenerInfos != null) {
            listenerInfos.forEach(listenerInfo -> {
                try {
                    listenerInfo.method.invoke(listenerInfo.target, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.log(Level.SEVERE, "Problem invoking listener", e);
                }
            });
        }
    }

    private static final class ListenerInfo {
        /** Object reference to the listener */
        public final Object target;
        /** Listener method to invoke */
        public final Method method;
        /** The event type **/
        public final Class<? extends SWPEvent> eventType;

        public ListenerInfo(Object target, Method method, Class<? extends SWPEvent> eventType) {
            this.target = target;
            this.method = method;
            this.eventType = eventType;
        }
    }
}
