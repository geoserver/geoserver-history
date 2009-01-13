package org.geoserver.wfs.response;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper used to invoke ogr2ogr
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
public class OGRWrapper {

    private File outputDirectory;

    private OgrParameters format;

    private String ogrExecutable;

    public OGRWrapper(File outputDirectory, OgrParameters format, String ogrExecutable) {
        this.outputDirectory = outputDirectory;
        this.format = format;
        this.ogrExecutable = ogrExecutable;
    }

    public void convert(File inputData, String typeName, String crsName) throws IOException, InterruptedException {
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

        // run the process and grab the output for error reporting purposes
        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append("\n");
            sb.append(line);
        }
        int exitCode = p.waitFor();

        if (exitCode != 0)
            throw new IOException("ogr2ogr did not terminate successfully, exit code " + exitCode
                    + "\n" + sb);
    }

}
