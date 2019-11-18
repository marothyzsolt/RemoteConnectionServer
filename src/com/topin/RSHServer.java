package com.topin;

import com.topin.helpers.StreamGobbler;

import java.net.*;
import java.io.*;
import java.util.concurrent.Executors;

public class RSHServer  {


    public static void main(String[] args) {
        /*try {
            Process proc = null;
            StringWriter output = new StringWriter();
            proc = Runtime.getRuntime().exec(new String("cmd.exe /c dir"));
            StreamGobbler streamGobbler = new StreamGobbler(proc.getInputStream(), output);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = proc.waitFor();
            String outputString = output.toString();
            System.out.println(outputString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
*/


        System.out.println("Starting...");
        try {
            ServerSocket server = new ServerSocket(7777);
            while (true) {
                Socket client = server.accept();
                new Thread(new SocketReader(client)).start();
                System.out.println("RSHServer servicing " + client);

               /* Socket client = server.accept();
                InputStream is = client.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                while(true){
                    System.out.println(br.readLine());
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}