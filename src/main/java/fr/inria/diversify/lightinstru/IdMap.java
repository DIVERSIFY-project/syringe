package fr.inria.diversify.lightinstru;

import java.util.HashMap;

/**
 * Created by marodrig on 12/12/2014.
 */
public class IdMap extends HashMap <String, Integer> {

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
