package com.SEG2505_Group8.mealer.Database.Utils;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Remove a database listener by calling remove()
 */
public class DatabaseListener {

    final List<ListenerRegistration> listenerRegistrations = new ArrayList<>();

    /**
     * Create an empty database listener
     */
    public DatabaseListener() {
    }

    /**
     * Create a database listener from a firebase listener registration
     * @param registration
     */
    public DatabaseListener(ListenerRegistration registration) {
        listenerRegistrations.add(registration);
    }

    /**
     * Create a database listener from multiple firebase listener registration
     * @param registrations
     */
    public DatabaseListener(List<ListenerRegistration> registrations) {
        listenerRegistrations.addAll(registrations);
    }

    /**
     * Remove the listener
     */
    public void remove() {
        for (ListenerRegistration r : listenerRegistrations) {
            r.remove();
        }
    }

    public void addRegistration(ListenerRegistration registration) {
        listenerRegistrations.add(registration);
    }

    public void addRegistration(DatabaseListener listener) {
        listenerRegistrations.addAll(listener.listenerRegistrations);
    }
}
