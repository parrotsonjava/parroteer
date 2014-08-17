package com.dronecontrol.droneapi.context;

import com.dronecontrol.droneapi.*;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.dronecontrol.droneapi.components.ErrorListenerComponent;

public class ParrotControlContext extends AbstractModule {
    private static Injector injector;

    public static <T> T getBean(Class<T> clazz) {
        if (injector == null) {
            injector = Guice.createInjector(new ParrotControlContext());
        }
        return injector.getInstance(clazz);
    }

    // Used for value builder
    @SuppressWarnings("UnusedDeclaration")
    protected Injector getInjector() {
        return injector;
    }

    @Override
    protected void configure() {
        bind(ParrotDroneController.class).in(Singleton.class);
        bind(DroneStartupCoordinator.class).in(Singleton.class);
        bind(CommandSender.class).in(Singleton.class);
        bind(CommandSenderCoordinator.class).in(Singleton.class);
        bind(InternalStateWatcher.class).in(Singleton.class);
        bind(NavigationDataRetriever.class).in(Singleton.class);
        bind(VideoRetrieverH264.class).in(Singleton.class);
        bind(VideoRetrieverP264.class).in(Singleton.class);
        bind(ConfigurationDataRetriever.class).in(Singleton.class);
        bind(ErrorListenerComponent.class).in(Singleton.class);
    }
}