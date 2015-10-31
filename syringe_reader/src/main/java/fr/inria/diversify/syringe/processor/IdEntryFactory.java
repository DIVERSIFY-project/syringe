package fr.inria.diversify.syringe.processor;

import java.util.HashMap;

/**
 * A factory to build entries
 */
public class IdEntryFactory extends EntryFactory{

    @Override
    public EntryLog build(String file, int line, HashMap<Integer, String> idMap) {
        return new IdEntry(file, line, idMap);
    }

}
