package fr.inria.diversify.syringe.processor;

import java.util.HashMap;

/**
 * Created by marodrig on 20/02/2015.
 */
public class IdEntry extends EntryLog {

    private long id;

    public IdEntry(String file, int line, HashMap<Integer, String> idMap) {
        super(file, line, idMap);
    }

    @Override
    protected void fromLineData(String[] lineData) throws LoadingException {
        super.fromLineData(lineData);
        if ( lineData.length > 2 ) {
            try {
                id = Long.parseLong(lineData[2]);
            } catch (NumberFormatException e) {
                throw new LoadingException(-1, lineData[2], e);
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
