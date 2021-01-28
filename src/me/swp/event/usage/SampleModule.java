package me.swp.event.usage;

import me.swp.event.SWPHandler;

/**
 * @author github.com/SWP360
 * @since 28/1/2021
 */

public class SampleModule {
    public SampleModule() {
        //self explanatory..
    }

    @SWPHandler
    public void onExample(ExampleEvent event) {
        System.out.println(event.getNumber());
        System.out.println(event.getText());
        event.cancelEvent(true);
    }
}
