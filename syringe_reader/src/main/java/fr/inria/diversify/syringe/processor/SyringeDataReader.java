package fr.inria.diversify.syringe.processor;


import java.io.*;
import java.util.*;

/**
 * TODO: Refactor this to the Syringe project
 * <p/>
 * Parent class of all classes in charge of reading metadata created using Syringe related to transformations
 * <p/>
 * Created by marodrig on 03/02/2015.
 */
public class SyringeDataReader {

    /**
     * Factory to create entries
     */
    private final EntryFactory factory;

    /**
     * Proccesor to post process the entries readed
     */
    private List<EntryProcessor> processors;

    /**
     * Collection of entries
     */
    private Collection<EntryLog> entries;

    public SyringeDataReader(EntryProcessor processor) {
        this(new EntryFactory(), processor);
    }

    public SyringeDataReader(EntryFactory factory, EntryProcessor processor) {
        this.processors = new ArrayList<>();
        this.processors.add(processor);
        this.factory = factory;
    }

    public SyringeDataReader(EntryFactory factory, List<EntryProcessor> processor) {
        this.processors = new ArrayList<>();
        this.processors.addAll(processors);
        this.factory = factory;
    }

    /**
     * Read the messy set of files output from Syringe
     *
     * @param logDirectory Directory where the Syringe files are
     */
    public Collection<EntryLog> read(String idFileName, String logDirectory) throws FileNotFoundException, LoadingException {

        ArrayList<EntryLog> result = new ArrayList<>();


        File absFile = new File(idFileName);
        if (!absFile.exists())
            absFile = new File(logDirectory + File.separator + idFileName).getAbsoluteFile();
        HashMap<Integer, String> idMap = readIdMap(new FileReader(absFile));

        absFile = new File(logDirectory).getAbsoluteFile();
        for (File f : absFile.listFiles()) {
            if (f.isFile() && (f.getName().endsWith(".log") || f.getName().startsWith("log"))) {
                try {
                    Collection<EntryLog> entries = read(idMap, f.getName(), new FileReader(f));
                    result.addAll(entries);
                } catch (LoadingException e) {
                    throw e;
                } catch (Exception e) {
                    throw new LoadingException(0, f.getName(), e);
                }
            }
        }

        Collections.sort(result);

        //
        for (EntryProcessor p : processors) p.process(result);
        return result;
    }

    /**
     * Reads a Id map
     *
     * @param reader Reader to read the ID map from
     * @return
     */
    protected HashMap<Integer, String> readIdMap(InputStreamReader reader) throws LoadingException {
        //Read the id file
        int iteration = 0;
        HashMap<Integer, String> idMap = new HashMap<>();
        try {
            //BufferedReader reader = new BufferedReader(new FileReader(logDir + File.separator + fileName));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                iteration++;
                String[] ln = line.split(" ");
                idMap.put(Integer.parseInt(ln[0]), ln[1]);
            }
        } catch (Exception e) {
            throw new LoadingException(iteration, "Id Map file", e);
        }
        return idMap;
    }

    /**
     * Read entries from stream
     *
     * @param reader Reader to read from the metadata
     * @param ids    Ids readed from the Id file
     * @return A collection of log entries organized by execution time
     * @fileName name of the file we are reading from. This is needed for login purposes
     */
    protected Collection<EntryLog> read(HashMap<Integer, String> ids, String fileName,
                                        InputStreamReader reader) throws LoadingException {
        ArrayList<EntryLog> result = new ArrayList<>();
        int iteration = 0;
        try {
            BufferedReader logReader = new BufferedReader(reader);
            String l;
            while ((l = logReader.readLine()) != null) {
                if (l.startsWith("#")) continue; //comments
                iteration++;
                String[] lineData;
                lineData = l.split(";");
                if (lineData.length > 0) {
                    EntryLog e = factory.build(fileName, iteration, ids);
                    e.fromLineData(lineData);
                    result.add(e);
                }
            }
        } catch (Exception e) {
            throw new LoadingException(iteration, fileName, e);
        }

        return result;
    }

    public Collection<EntryLog> getEntries() {
        return entries;
    }

    public List<String> getProcessingErrors() {
        ArrayList<String> errors = new ArrayList<>();
        for (EntryProcessor p : processors) errors.addAll(p.getErrors());
        return errors;
    }
}
