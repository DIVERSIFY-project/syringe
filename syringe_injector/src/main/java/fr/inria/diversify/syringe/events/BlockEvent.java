package fr.inria.diversify.syringe.events;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

import java.util.List;

/**
 * An event when an element containing a block is detected
 * <p/>
 * Created by marodrig on 14/12/2015.
 */
public class BlockEvent extends AbstractEvent {

    private CtElement element;
    /**
     * Detected method's block
     */
    CtBlock block;

    /**
     * First statement of the method
     */
    CtStatement firstStatement;


    /**
     * Last statement of the method
     */
    CtStatement lastStatement;

    /**
     * List of all return statements
     */
    List<CtReturn> returnStatements;

    public BlockEvent(CtElement element, CtBlock block, CtStatement firstStatement,
                      CtStatement lastStatement, List<CtReturn> returnStatements) {
        this.element = element;
        this.block = block;
        this.firstStatement = firstStatement;
        this.lastStatement = lastStatement;
        this.returnStatements = returnStatements;
    }

    public BlockEvent() {

    }

    public List<CtReturn> getReturnStatements() {
        return returnStatements;
    }

    public CtBlock getBlock() {
        return block;
    }

    public CtStatement getFirstStatement() {
        return firstStatement;
    }

    public CtStatement getLastStatement() {
        return lastStatement;
    }



    public void setBlock(CtBlock block) {
        this.block = block;
    }

    public void setFirstStatement(CtStatement firstStatement) {
        this.firstStatement = firstStatement;
    }

    public void setLastStatement(CtStatement lastStatement) {
        this.lastStatement = lastStatement;
    }

    public void setReturnStatements(List<CtReturn> returnStatements) {
        this.returnStatements = returnStatements;
    }
}
