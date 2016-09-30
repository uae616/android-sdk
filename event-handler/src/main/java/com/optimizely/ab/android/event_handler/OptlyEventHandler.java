package com.optimizely.ab.android.event_handler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.optimizely.ab.event.EventHandler;
import com.optimizely.ab.event.LogEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jdeffibaugh on 7/21/16 for Optimizely.
 *
 * Reference implementation of {@link EventHandler} for Android.
 *
 * This is the main entry point to the Android Module
 */
public class OptlyEventHandler implements EventHandler {

    Logger logger = LoggerFactory.getLogger(OptlyEventHandler.class);

    @NonNull private final Context context;
    private long dispatchInterval = -1;

    public static OptlyEventHandler getInstance(@NonNull Context context) {
        return new OptlyEventHandler(context);
    }

    /**
     * Constructs a new instance
     * @param context any valid Android {@link Context}
     */
    private OptlyEventHandler(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Sets event dispatch interval
     *
     * Events will only be scheduled to dispatch as long as events remain in storage.
     *
     * Events are put into storage when they fail to send over network.
     * @param dispatchInterval the interval in the provided {@link TimeUnit}
     * @param timeUnit a {@link TimeUnit}
     */
    public void setDispatchInterval(long dispatchInterval, TimeUnit timeUnit) {
        this.dispatchInterval = timeUnit.toMillis(dispatchInterval);
    }

    @Override
    public void dispatchEvent(LogEvent logEvent) {
        if (logEvent.getEndpointUrl() == null) {
            logger.error("Event dispatcher received a null url");
            return;
        }
        if (logEvent.getBody() == null) {
            logger.error("Event dispatcher received a null request body");
            return;
        }
        if (logEvent.getEndpointUrl().isEmpty()) {
            logger.error("Event dispatcher received an empty url");
        }

        Intent intent = new Intent(context, EventIntentService.class);
        intent.putExtra(EventIntentService.EXTRA_URL, logEvent.getEndpointUrl());
        intent.putExtra(EventIntentService.EXTRA_REQUEST_BODY, logEvent.getBody());
        if (dispatchInterval != -1) {
            intent.putExtra(EventIntentService.EXTRA_INTERVAL, dispatchInterval);
        }
        context.startService(intent);
        logger.info("Sent url {} to the event handler service", logEvent.getEndpointUrl());

    }
}