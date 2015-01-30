package fr.inria.diversify.lightinstru.detectors;

import java.util.HashMap;

/**
 * Data to encapsulate the detection parameters
 *
 * Created by marodrig on 09/12/2014.
 */
public class DetectionData {

    //Indicate if it is paired with another detection
    private HashMap<String, Integer> idMap;
    private Integer elementId;

    private boolean endWithSemiColon;
    /**
     * Detection data map constructor with the id
     *
     * @param map Map containing all ids.
     */
    public DetectionData(HashMap<String, Integer> map) {
        idMap = map;
        endWithSemiColon = true;
    }

    public DetectionData() {
        endWithSemiColon = true;
    }

    public HashMap<String, Integer> getIdMap() {
        return idMap;
    }

    public void setIdMap(HashMap<String, Integer> idMap) {
        this.idMap = idMap;
    }

    /**
     * ID of the last element detected
     * @param elementId
     */
    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    /**
     * ID of the last element detected
     */
    public Integer getElementId() {
        return elementId;
    }

    public boolean isEndWithSemiColon() {
        return endWithSemiColon;
    }

    public void setEndWithSemiColon(boolean endWithSemiColon) {
        this.endWithSemiColon = endWithSemiColon;
    }
}
