package fr.inria.diversify.lightinstru;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Turn the log into an abstract representation of the program's execution
 *
 * Created by marodrig on 28/12/2014.
 */
public abstract class LogAnalysis {

    //Id map of the ids of the entries
    private HashMap<Integer, String> idMap;

    /**
     * Factory method to gives the opportunity to the descendants of this class to create custom EntryLogs
     * @return
     */
    protected abstract EntryLog getNewEntryLog();


    /**
     * Reads the id map that goes with the instrumentation process
     * @param idFileName File name of the id file name
     */
    private void readIdMap(String idFileName) {
        idMap = new HashMap<Integer, String>();

        //Read the id file
        int iteration = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(idFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                iteration++;
                String[] ln = line.split(" ");
                idMap.put(Integer.parseInt(ln[0]), ln[1]);
            }
        } catch (Exception e) {
            throw new LoadLogException(iteration, idFileName, e.getMessage());
        }
    }

    public void analyze(String logDir, String idFileName) {
        analyze(collectEntries(logDir, idFileName));
    }

    /**
     * Load all entries and sort them so we can have a linear representation of the program's execution
     *
     * TODO: Create a real parallel representation of the program's execution
     *
     * @param logDir Directory of the log files
     * @param idFileName File name of the id file name
     *
     * @return A sorted collection of EntryLogs
     */
    protected Collection<EntryLog> collectEntries(String logDir, String idFileName) {
        //Read the id map
        String[] path = new String[]{idFileName, logDir + File.separator + idFileName};
        for ( String p : path ) {
            File f = new File(p);
            if ( f.exists() ) readIdMap(f.getAbsolutePath());
        }

        if ( idMap == null ) throw new RuntimeException("Unable to find id map");

        //Collect all the entry logs in different files to sort them by execution time
        List<EntryLog> entries = new ArrayList<EntryLog>();

        int iteration = 0;
        String fileName = "";
        for (File f : new File(logDir).listFiles()) {
            if (f.getName().startsWith("log")) {
                try {
                    fileName = f.getName();
                    iteration = 0;
                    BufferedReader logReader = new BufferedReader(new FileReader(f));
                    String l;
                    while ((l = logReader.readLine()) != null) {
                        iteration++;
                        String[] lineData = l.split(";");
                        if (lineData.length > 0) {
                            EntryLog e = getNewEntryLog();
                            e.fromLine(iteration, fileName, lineData, idMap);
                            entries.add(e);
                        }
                    }
                } catch (Exception e) {
                    throw new LoadLogException(iteration, fileName, e);
                }
            }
        }

        Collections.sort(entries);
        return entries;
    }

    /**
     * Analyze the entries collected
     * @param entries Entries collected
     */
    protected abstract void analyze(Collection<EntryLog> entries);
}
