package fr.inria.diversify.syringe.signature;

import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

/**
 * A signature returning only the class of the element
 *
 * Created by marodrig on 20/02/2015.
 */
public class ClassSignature implements SignatureGenerator {

    @Override
    public String getSignature(CtElement e) {
        return e.getPosition().getCompilationUnit().getMainType().getQualifiedName();
    }
}
