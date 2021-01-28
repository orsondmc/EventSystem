package me.swp.event.usage;

import me.swp.event.EventBus;

/**
 * @author github.com/SWP360
 * @since 28/1/2021
 */

public class Main {
    public static EventBus EVENT_BUS;

    public static void main(String[] args) {
        EVENT_BUS = new EventBus();
        EVENT_BUS.register(new SampleModule());

        ExampleEvent event = new ExampleEvent(20, "Hello!");
        EVENT_BUS.dispatch(event);
        System.out.println(event.isEventCancelled());
    }
}
