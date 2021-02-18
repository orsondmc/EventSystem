package me.swp.event.usage;

import me.swp.event.EventBus;

/**
 * @author github.com/SWP360
 * @since 28/1/2021
 */

public class Main {

    public static void main(String[] args) {
        EventBus bus = new EventBus();
        bus.register(new SampleModule());

        ExampleEvent event = new ExampleEvent(20, "Hello!");
        bus.dispatch(event);
        System.out.println(event.isEventCancelled());
    }
}
