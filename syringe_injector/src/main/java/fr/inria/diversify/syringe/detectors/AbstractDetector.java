package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.IdMap;
import fr.inria.diversify.syringe.injectors.Injector;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by marodrig on 01/11/2015.
 */
public abstract class AbstractDetector<E extends CtElement> extends AbstractProcessor<E>
        implements Detector<E> {
    /**
     * Injectors listening to an event. Ordered by event.
     */
    private HashMap<String, Collection<Injector>> injectors;

    private IdMap idMap;

    /**
     * In order to save space in the log files is often useful to store an id instead of the full signature
     *
     * @param idMap
     */
    public void setIdMap(IdMap idMap) {
        this.idMap = idMap;
    }

    /**
     * In order to save space in the log files is often useful to store an id instead of the full signature
     */
    public IdMap getIdMap() {
        return idMap;
    }


    /**
     * Add an injector to inject code in a given event
     *
     * @param eventName
     * @param injector
     */
    public void addInjector(String eventName, Injector injector) {
        if (injectors == null) injectors = new HashMap<>();
        if (!injectors.containsKey(eventName)) injectors.put(eventName, new ArrayList<Injector>());
        injectors.get(eventName).add(injector);
    }

    /**
     * Removes an injector
     *
     * @param injector
     */
    public void removeInjector(Injector injector) {
        if (injectors == null) return;
        for (Collection<Injector> c : injectors.values()) {
            if (c.contains(injector)) c.remove(injector);
        }
    }

    @Override
    public void notify(String eventName, CtElement detection, DetectionData data) {
        if (injectors == null) return;
        for (Injector injector : injectors.get(eventName)) {
            injector.inject(detection, data);
        }
    }
}
