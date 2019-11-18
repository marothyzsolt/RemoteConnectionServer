package com.topin.helpers;

import java.io.*;

public class StreamGobbler extends Thread
{
    private InputStream is;
    StringWriter stringWriter;

    public StreamGobbler(InputStream is, StringWriter stringWriter)
    {
        this.is = is;
        this.stringWriter = stringWriter;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ( (line = br.readLine()) != null)
            {
                //System.out.println(line);
                this.stringWriter.append(line);
                this.stringWriter.append("\n");
            }
            this.stringWriter.flush();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}