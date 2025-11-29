package com.stockstreaming.demo.listeners;

import com.stockstreaming.demo.events.FileProcessedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class NotificationListener {

    @Async
    @EventListener
    public void notifyUser(FileProcessedEvent event) throws Exception{
        Thread.sleep(10000);
        System.out.println("Notification sent for file ID: " + event.fileId());
    }
}
