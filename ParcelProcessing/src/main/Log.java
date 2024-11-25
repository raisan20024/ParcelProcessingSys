package main;

public class Log {
    private static Log instance;
    private StringBuffer logBuffer;
    private Log() {
        logBuffer = new StringBuffer();
    }
    public static synchronized Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void addEvent(String event) {
        logBuffer.append(event).append("\n");
    }
    public void addLog(String event) {
        addEvent(event);
    }
    public String getLog() {
        return logBuffer.toString();
    }
    public void clearLog() {
        logBuffer.setLength(0);
    }
    public void writeLogToFile(String filename) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write(logBuffer.toString());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return logBuffer.toString();
    }
}
