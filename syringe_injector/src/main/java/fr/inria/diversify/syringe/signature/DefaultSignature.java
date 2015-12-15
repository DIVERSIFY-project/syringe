package fr.inria.diversify.syringe.signature;

import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

/**
 * A signature uniquely identifying an element given its position in the source in the form <class>:<lineNumber>
 *
 * In order to uniquely identify an element is necessary to provide a signature. Default spoon signatures
 * are not enough in this case because two semantically equal elements in different positions
 * would have the same signature. Our signature must also include the position of the element.
 *
 * Depending on the detection case, two elements can be equal even if they are not the same. In this case,
 * you can modify its signature.
 *
 *
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
