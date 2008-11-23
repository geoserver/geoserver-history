package org.geoserver.filters;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Wrap a String up as a ServletInputStream so we can read it multiple times.
 * @author David Winslow <dwinslow@openplans.org>
 */
public class BufferedRequestStream extends ServletInputStream{
    InputStream myStream;

    public BufferedRequestStream(String buff) throws IOException {
        myStream = new ByteArrayInputStream(buff.getBytes());
        myStream.mark(16);
        myStream.read();
        myStream.reset();
    }

    public int readLine(byte[] b, int off, int len) throws IOException{
        int read; 
        int index = off;
        int end = off + len;

        while (index < end && 
                (read = myStream.read()) != -1){
            b[index] = (byte)read; 
            index++;
            if (((char)read)== '\n'){
                break;
            }
        }

        return index - off;
    }

    public int read() throws IOException{
        return myStream.read();
    }

    public int available() throws IOException {
        return myStream.available();
    }
}
