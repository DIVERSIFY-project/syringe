package fr.inria.diversify.syringe.processor;

/**
 * Loading exceptions
 * <p/>
 * Created by marodrig on 04/09/2014.
 */
public class LoadingException extends Exception {

    private int lineNumber;

    private String fileName;

    public LoadingException(int iteration, String fileName, String msg) {
        super(msg);
        this.lineNumber = iteration;
        this.fileName = fileName;
    }

    public LoadingException(int iteration, String fileName, Exception cause) {
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
        return "In file:" + fileName + ", At iteration:" + lineNumber + ", got" + super.getMessage();
    }
}

