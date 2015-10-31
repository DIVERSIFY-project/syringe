import fr.inria.diversify.syringe.processor.EntryLog;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by marodrig on 22/02/2015.
 */
public class EntryLogTest {

    @Test
    public void testNormalReading() throws Exception {
        EntryLog entryLog = new EntryLog("file/myFile/", 1, new HashMap<Integer, String>());
        entryLog.fromLine("T;123345567;1", ";");
        assertEquals("T", entryLog.getType());
        assertEquals(123345567, entryLog.getMillis());
    }
}
