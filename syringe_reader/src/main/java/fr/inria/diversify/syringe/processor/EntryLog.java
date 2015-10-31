package fr.inria.diversify.syringe.processor;

import java.util.HashMap;
import java.util.Objects;

/**
 * Base class to all log entries from Syringe
 * <p>
 * Created by marodrig on 03/02/2015.
 */
public class EntryLog implements Comparable {

    protected HashMap<Integer, String> idMap;

    protected String type;

    protected long millis;

    protected String fileName;

    protected int iteration = 0;
    //position or position id
    protected String position;

    protected String[] parameters;

    public EntryLog(String file, int line, HashMap<Integer, String> idMap) {
        this.iteration = line;
        fileName = file;
        this.idMap = idMap;
    }


    @Override
    public int compareTo(Object o) {
        EntryLog e = (EntryLog) o;
        long k = getMillis() - e.getMillis();
        if (k == 0) return 0;
        else if (k < 0) return -1;
        else return 1;
    }

    @Override
    public String toString() {
        return getType() + "," + getPosition() + "," + getMillis();
    }

    /**
     * Extract the information from a line
     *
     * @param line      Line containing data
     * @param separator Fields separator
     */
    public void fromLine(String line, String separator) throws LoadingException {
        fromLineData(line.split(separator));
    }

    /**
     * Extract the data from the spliced login string
     */
    protected void fromLineData(String[] lineData) throws LoadingException {
        type = lineData[0];
        millis = Long.parseLong(lineData[1]);
        if (lineData.length > 2) {
            parameters = new String[lineData.length - 2];
            for (int i = 2; i < lineData.length; i++) parameters[i - 2] = lineData[i];
        }
    }

    /**
     *
     * @return
     */
    public HashMap<Integer, String> getIdMap() {
        return idMap;
    }


    /**
     * Type of the logging element
     */
    public String getType() {
        return type;
    }

    /**
     * Type of the execution of the lg
     */
    public long getMillis() {
        return millis;
    }

    /**
     * Text file from which this element was extracted
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Number of the line in the text file where this entry was located
     */
    public int getIteration() {
        return iteration;
    }

    public String getPosition() {
        return position;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    /*
        if (type.equals(END_TEST)) {
            millis = Long.parseLong(lineData[1]);
            return;
        }
        String p = idMap.get(Integer.parseInt(lineData[1]));
        position = p.substring(p.indexOf(">") + 1);
        if (type.equals(NEW_TEST) || type.equals(ASSERT)) {
            millis = Long.parseLong(lineData[2]);

        } else if (type.equals(TP_COUNT) || type.equals(ASSERT_COUNT)) {
            millis = Long.parseLong(lineData[2]);
            executions = Integer.parseInt(lineData[3]);
            if (type.equals(TP_COUNT)) {
                minDepth = Integer.parseInt(lineData[4]);
                meanDepth = Integer.parseInt(lineData[5]);
                maxDepth = Integer.parseInt(lineData[6]);
            }
        } else if (type.equals(TP)) {
            millis = Long.parseLong(lineData[2]);
            maxDepth = meanDepth = minDepth = Integer.parseInt(lineData[3]);
        }
    }*/
}