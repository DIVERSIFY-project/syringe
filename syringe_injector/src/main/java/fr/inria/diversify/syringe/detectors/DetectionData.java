package fr.inria.diversify.syringe.detectors;

import java.util.HashMap;

/**
 * Data to encapsulate the detection parameters
 *
 * Created by marodrig on 09/12/2014.
 */
public class DetectionData {

    /**
     * Get params
     *
     * @return a Hash map where the index is the parameter name and the value, the value of the param
     */
    public HashMap<String, String> getParams() {
        if ( params == null ) params = new HashMap<>();
        return params;
    }

    /**
     * Params to substitute in the injection string
     */
    private HashMap<String, String> params;

    /**
     * Indicate if it is paired with another detection like in the case of a opening and a close bracket,
     * the begin and the end of a loop, block, etc.
     */
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
        params = new HashMap<>();
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
        getParams().put("elementId", elementId.toString());
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
