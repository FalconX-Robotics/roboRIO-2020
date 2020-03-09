package frc.robot.commands;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

public class DebugAssistant {
    protected PrintWriter writer;
    protected String outputFileName;
    protected int initCount = 0;
    protected int executeCount = 0;
    protected int isFinished = 0;
    protected int endCount = 0;
    protected HashMap<String, Integer> countMap = new HashMap<>();
    protected HashMap<String, Integer> countLimitMap = new HashMap<>();

    protected String br = "-----------------------------------";

    public DebugAssistant(String outputFileName) {
        try {
            writer = new PrintWriter(new FileWriter("./logs/" + outputFileName + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        writer.println("debug assistant instanced");
        writer.println("file name: " + outputFileName);
        writer.println(br);
        writer.flush();

        this.outputFileName = outputFileName;
    }

    public void trackInitialize() {
        initCount++;
        writer.println("initialize() called for " + initCount);
        writer.flush();
    }

    public void trackExecute() {
        executeCount++;
        writer.println("execute() called for " + executeCount);
        writer.flush();
    }

    public void trackEnd(boolean interrupted) {
        endCount++;
        writer.println("end() called for " + endCount);
        writer.println(br);
        writer.flush();
    }

    /**
     * This method also clears all the tracked variables.
     */
    public boolean trackIsFinished() {
        isFinished++;
        writer.println("isFinished() called for " + endCount);
        writer.flush();
        countMap.clear();
        return false;
    }

    public void print(String message) {
        writer.print(message);
        writer.flush();
    }

    public void println(String message) {
        writer.println(message);
        writer.flush();
    } 

    public void printVar(String name, Object value) {
        writer.println(name + ": " + value.toString());
        writer.flush();
    }

    public void printBr() {
        writer.println(br);
    }

    public void addTrackCount(String name, int increment) {
        checkTrackCountLimit(name);
        int count = countMap.getOrDefault(name, 0) + increment;
        countMap.put(name, count);
        writer.println(name + ": " + count + " [tracked count]");
        writer.flush();
    }

    public void trackCount(String name) {
        addTrackCount(name, 1);
    }

    public void clearTrackCount(String name) {
        addTrackCount(name, -countMap.getOrDefault(name, 0));
    }

    public void clearAllTrackCounts() {
        countMap.clear();
    }

    public void checkTrackCountLimit(String name) {
        if (countMap.getOrDefault(name, 0) > countLimitMap.getOrDefault(name, Integer.MAX_VALUE)) {
            failAssert("track_limit", name, countMap.getOrDefault(name, 0));
        }
    }

    /**
     * Sets a count limit to name.
     * Asserts false when count is above (but not equal) to limit.
     */
    public void setTrackLimit(String name, int limit) {
        countLimitMap.put(name, limit);
    }

    public void passAssert(String methodName, String id, Object a, Object b) {
        writer.println("assert " + methodName.toUpperCase() + " passed [" + id + "]: " + a + ", " + b);
        writer.flush();
    }

    public void passAssert(String methodName, String id, Object a) {
        writer.println("assert " + methodName.toUpperCase() + " passed [" + id + "]: " + a);
        writer.flush();
    }

    public void passAssert(String methodName, String id, Object[] a, Object[] b) {
        passAssert(methodName, id, Arrays.toString(a), Arrays.toString(b));
    }

    public void failAssert(String methodName, String id, Object a, Object b) {
        writer.println("!ASSERT " + methodName.toUpperCase() + " FAILED [" + id + "]: " + a + ", " + b);
        writer.flush();
    }

    public void failAssert(String methodName, String id, Object a) {
        writer.println("!ASSERT " + methodName.toUpperCase() + " FAILED [" + id + "]: " + a);
        writer.flush();
    }

    public void failAssert(String methodName, String id, Object[] a, Object[] b) {
        failAssert(methodName, id, Arrays.toString(a), Arrays.toString(b));
    }

    public boolean assertEquals(String id, Object a, Object b) {
        boolean isEqual = a.equals(b);
        if (!isEqual) {
            failAssert("equals", id, a, b);
        } else {
            passAssert("equals", id, a, b);
        }
        return !isEqual;
    }

    public boolean assertNotEquals(String id, Object a, Object b) {
        boolean isEqual = a.equals(b);
        if (isEqual) {
            failAssert("not_equals", id, a);
        } else {
            passAssert("not_equals", id, a);
        }
        return isEqual;
    }

    public boolean assertArrayEquals(String id, Object[] a, Object[] b) {
        boolean passed = true;
        if (a.length == b.length) {
            for (int i = 0; i < a.length; i++) {
                passed = passed && a[i].equals(b[i]);
            }
        } else {
            passed = false;
        }
        if (passed) {
            passAssert("array_equals", id, a, b);
        } else {
            failAssert("array_equals", id, a, b);
        }
        return true;
    }

    public boolean assertTrue(String id, boolean a) {
        if (!a) {
            failAssert("true", id, a);
        } else {
            passAssert("true", id, a);
        }
        return !a;
    }

    public boolean assertFalse(String id, boolean a) {
        if (a) {
            failAssert("false", id, a);
        } else {
            passAssert("false", id, a);
        }
        return a;
    }

    public boolean assertNull(String id, Object a) {
        if (a != null) {
            failAssert("null", id, a);
        } else {
            passAssert("null", id, a);
        }
        return a != null;
    }

    public boolean assertNotNull(String id, Object a) {
        if (a == null) {
            failAssert("not_null", id, a);
        } else {
            passAssert("not_null", id, a);
        }
        return a == null;
    }

}