package org.geoserver.filters;

import javax.servlet.ServletInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.Reader;

/**
 * Wrap a String up as a ServletInputStream so we can read it multiple times.
 * @author David Winslow <dwinslow@openplans.org>
 */
public class BufferedRequestStream extends ServletInputStream{
    Reader myReader;

    public BufferedRequestStream(String buff){
        myReader = new StringReader(buff);
    }

    public int readLine(byte[] b, int off, int len) throws IOException{
        int read; 
        int index = off;
        int end = off + len;

        while (index < end && 
                (read = myReader.read()) != -1){
            b[index] = (byte)read; 
            index++;
            if (((char)read)== '\n'){
                break;
            }
        }

        return index - off;
    }

    public int read() throws IOException{
        return myReader.read();
    }
}
