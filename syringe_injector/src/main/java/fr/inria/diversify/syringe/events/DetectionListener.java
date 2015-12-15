package fr.inria.diversify.syringe.events;

import fr.inria.diversify.syringe.events.DetectionEvent;
import spoon.reflect.declaration.CtElement;

/**
 * Interface to all injectors. An injector is a class that injects code at specific points
 *
 * Created by marodrig on 31/10/2015.
 */
public interface DetectionListener {

    void listen(DetectionEvent data);

}
