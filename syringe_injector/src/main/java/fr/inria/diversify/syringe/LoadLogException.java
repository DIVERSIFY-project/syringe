package fr.inria.diversify.syringe;

/**
 * Loading exceptions
 * <p/>
 * Created by marodrig on 04/09/2014.
 */
public class LoadLogException extends RuntimeException {

    private int lineNumber;

    private String fileName;

    public LoadLogException(int iteration, String fileName, String msg) {
        super(msg);
        this.lineNumber = iteration;
        this.fileName = fileName;
    }

    public LoadLogException(int iteration, String fileName, Exception cause) {
        super(cause);
        this.lineNumber = iteration;
        this.fileName = fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMessage() {
        return "Line:" + lineNumber + " At: " + fileName + ". Error" + super.getMessage();
    }
}

