package fr.inria.diversify.syringe;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.HashMap;

/**
 * An entry of the log files
 * <p>
 * Created by marodrig on 28/12/2014.
 */
public class EntryLog implements Comparable {

    /**
     * Name of the file where the entry log was recovered.
     */
    private String fileName;

    /**
     * Type of the element logged . Can be anything "Test", "Branch", "Line", but also "Warn", "Caviar", "SpaceShip"...
     * You name it, it will depend on the application
     */
    private String type;

    /**
     * ID of the element being logged.
     */
    private int id;

    /**
     * Time at wich the element was taken
     */
    private long millis;

    /**
     * Number of the line in the file
     */
    private int lineNumber;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    /**
     * Extract the entry log information out a line of text
     *
     * @param lineNumber Number of the line in the file
     * @param fileName   Name of the log file from which this entry was obtained
     * @param line       Text line
     * @param idMap      Id map of the entry elements
     */
    public void fromLine(int lineNumber, String fileName, String[] line, HashMap<Integer, String> idMap) {
        this.lineNumber = lineNumber;
        this.fileName = fileName;

        type = line[0];
        millis = Long.parseLong(line[1]);
        if (line.length > 2) id = Integer.parseInt(line[2]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Line: ").append(lineNumber).append(" at ").append(fileName).append("Type: ").append(type);
        sb.append(" id: ").append(id).append(" millis:").append(millis);
        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        EntryLog el = (EntryLog) o;
        int r = (int) (millis - el.millis);
        return r == 0 ? lineNumber - el.lineNumber : r;
    }
}
