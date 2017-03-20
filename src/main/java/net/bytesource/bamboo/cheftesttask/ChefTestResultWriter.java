package net.bytesource.bamboo.cheftesttask;

import com.atlassian.utils.process.ExternalProcess;
import org.apache.log4j.Logger;

import java.io.*;

public class ChefTestResultWriter {
    private static final Logger LOGGER = Logger.getLogger(ChefTestResultWriter.class);

    private ChefTestResultWriter() {

    }

    public static void writeStringToFile(String filename, String result, Boolean append) {

        Writer out = null;
        try {
            out = new OutputStreamWriter(
                    new FileOutputStream(filename, append), "UTF-8");
            out.write(result);
            out.close();
        } catch (IOException ioe) {
            LOGGER.error("IOException: " + ioe);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }

    public static void writeProcessResults(ExternalProcess process, String filename) {
        ChefTestResultWriter.writeStringToFile(filename, "-1", false);
        long startTime = System.currentTimeMillis();
        process.execute();
        ChefTestResultWriter.writeStringToFile(filename, String.valueOf(process.getHandler().getExitCode()), false);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        ChefTestResultWriter.writeStringToFile(filename, ";" + duration, true);
    }
}
