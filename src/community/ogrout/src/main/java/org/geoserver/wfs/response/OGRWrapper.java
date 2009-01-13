package org.geoserver.wfs.response;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

/**
 * Helper used to invoke ogr2ogr
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
public class OGRWrapper {

    private static final Logger LOGGER = Logging.getLogger(OGRWrapper.class);

    private String ogrExecutable;

    public OGRWrapper(String ogrExecutable) {
        this.ogrExecutable = ogrExecutable;
    }

    public void convert(File inputData, File outputDirectory, String typeName,
            OgrParameters format, String crsName) throws IOException, InterruptedException {
        // build the command line
        List<String> cmd = new ArrayList<String>();
        cmd.add(ogrExecutable);
        cmd.add("-f");
        cmd.add(format.ogrFormat);
        if (crsName != null) {
            cmd.add("-a_srs");
            cmd.add(crsName);
        }
        if (format.options != null) {
            for (String option : format.options) {
                cmd.add(option);
            }
        }
        String outFileName = typeName;
        if (format.fileExtension != null)
            outFileName += format.fileExtension;
        cmd.add(new File(outputDirectory, outFileName).getAbsolutePath());
        cmd.add(inputData.getAbsolutePath());

        StringBuilder sb = new StringBuilder();
        int exitCode = run(cmd, sb);

        if (exitCode != 0)
            throw new IOException("ogr2ogr did not terminate successfully, exit code " + exitCode
                    + "\n" + sb);
    }

    /**
     * Returns a list of the ogr2ogr supported formats
     * 
     * @return
     */
    public List<String> getSupportedFormats() {
        try {
            List<String> commands = new ArrayList<String>();
            commands.add(ogrExecutable);
            commands.add("--help");

            StringBuilder sb = new StringBuilder();
            int exitCode = run(commands, sb);

            if (exitCode != 0) {
                LOGGER
                        .warning("Could not get the list of output formats supported by ogr2ogr, output was:\n"
                                + sb);
                return Collections.emptyList();
            }

            List<String> formats = new ArrayList<String>();
            String[] lines = sb.toString().split("\n");
            for (String line : lines) {
                if (line.matches("\\s*-f \"")) {
                    String format = line.substring(line.indexOf('"'), line.lastIndexOf('"'));
                    formats.add(format);
                }
            }

            Collections.sort(formats);
            return formats;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Could not get the list of output formats supported by ogr2ogr", e);
            return Collections.emptyList();
        }
    }

    /**
     * Returns true if ogr2ogr is available, that is, if executing
     * "ogr2ogr --version" returns 0 as the exit code
     * 
     * @return
     */
    public boolean isAvailable() {
        List<String> commands = new ArrayList<String>();
        commands.add(ogrExecutable);
        commands.add("--version");

        try {
            return run(commands, null) == 0;
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Ogr2Ogr is not available", e);
            return false;
        }
    }

    /**
     * Runs the specified command appending the output to the string builder and
     * returning the exit code
     * 
     * @param cmd
     * @param sb
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    int run(List<String> cmd, StringBuilder sb) throws IOException, InterruptedException {
        // run the process and grab the output for error reporting purposes
        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (sb != null) {
                sb.append("\n");
                sb.append(line);
            }
        }
        return p.waitFor();
    }
}
