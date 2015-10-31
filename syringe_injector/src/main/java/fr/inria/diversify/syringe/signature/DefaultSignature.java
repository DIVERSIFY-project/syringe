package fr.inria.diversify.syringe.signature;

import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

/**
 * A signature uniquely identifying an element given its position in the source in the form <class>:<lineNumber>
 *
 * Created by marodrig on 20/02/2015.
 */
public class DefaultSignature implements SignatureGenerator {
    @Override
    public String getSignature(CtElement e) {
        SourcePosition sp = e.getPosition();
        CompilationUnit cu = sp.getCompilationUnit();
        return "<"+e.getClass().getSimpleName()+">" + cu.getMainType().getQualifiedName() + ":" + sp.getLine();
    }
}
