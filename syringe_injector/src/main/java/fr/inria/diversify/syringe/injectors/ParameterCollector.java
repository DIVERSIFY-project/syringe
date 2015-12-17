package fr.inria.diversify.syringe.injectors;

import spoon.reflect.declaration.CtElement;

import java.util.HashMap;

/**
 * A class to collect injection paramters for the Parametrized Snippet statement
 *
 * Created by marodrig on 15/12/2015.
 */
public abstract class ParameterCollector<E extends CtElement> {

    public abstract HashMap<String, Object> collectParameters(E element);

}
