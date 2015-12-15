package fr.inria.diversify.syringe.injectors;

import fr.inria.diversify.syringe.events.DetectionListener;

/**
 * Created by marodrig on 15/12/2015.
 */
public interface Injector extends DetectionListener {

    String getInjectionTemplate();

    void setInjectionTemplate(String s);

}
