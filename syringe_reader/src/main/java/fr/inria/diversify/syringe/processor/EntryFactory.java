package fr.inria.diversify.syringe.processor;

import java.util.HashMap;

/**
 * A factory to build entries
 */
public class EntryFactory {
    public EntryLog build(String file, int line, HashMap<Integer, String> idMap) {
        return new EntryLog(file, line, idMap);
    }
}
