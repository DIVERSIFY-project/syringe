package fr.inria.diversify.syringe.signature;

import spoon.reflect.declaration.CtElement;

/**
 *
 * In order to identify elements we use the signature Generator.
 *
 * For example let's asume we want to know the class from wich a method is called.
 *
 * Instead of assigning differents IDs to all methods, we want to assign the same ID than the class,
 * we customize the getID method
 *
 * Created by marodrig on 20/02/2015.
 */
public interface SignatureGenerator {

    /**
     * Gets an String signature from an element
     * @param e Element for which we want to extract a signature
     * @return A string with the signature
     */
    public String getSignature(CtElement e);

}
