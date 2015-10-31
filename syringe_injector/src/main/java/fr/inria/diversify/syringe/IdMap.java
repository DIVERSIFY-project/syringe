package fr.inria.diversify.syringe;

import java.util.HashMap;

/**
 * Created by marodrig on 12/12/2014.
 */
public class IdMap extends HashMap <String, Integer> {

    /**
     * Returns the last index given
     * @return
     */
    public int getLastIndex() {
        return index;
    }

    private int index = 0;

    /**
     * Adds to the map an auto-incremental index
     * @param id
     */
    public void addToMap(String id) {
        if ( !containsKey(id) ) {
            index++;
            put(id, index);
        }
    }

}
