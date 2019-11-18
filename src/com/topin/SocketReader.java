package com.topin;

import com.topin.helpers.StreamGobbler;

import java.io.*;
import java.net.Socket;

public class SocketReader implements Runnable {

    private Socket client;
    private BufferedOutputStream outputStream;

    public SocketReader(Socket client) throws IOException {
        this.client = client;
    }

    public void run() {
        try {

            this.outputStream = new BufferedOutputStream(client.getOutputStream());
            this.outputStream.write(147);
            this.outputStream.write(148);
            this.outputStream.flush();


            InputStream is = client.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            do {
                System.out.println(bufferedReader.readLine());
            } while (bufferedReader.readLine() != null);
        } catch (Exception e) {
            System.out.println("Connection closed (" + this.client + ")");
        }
    }

    public void run2() {
        Process proc;

        try {
            int x = 2;
            InputStream is = client.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            while (true) {
                System.out.println(br.readLine());
                this.outputStream = new BufferedOutputStream(client.getOutputStream());
                this.outputStream.write(147);
                this.outputStream.flush();

                if (br.readLine() == null) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(true) {
            String cmd = readData().toString();
            System.out.println("Command from client (" + client.getInetAddress() + "): " + cmd);

            try {
                this.outputStream = new BufferedOutputStream(client.getOutputStream());

                //proc = Runtime.getRuntime().exec(cmd);

                /*StringWriter output = new StringWriter();
                StreamGobbler streamGobbler = new StreamGobbler(proc.getInputStream(), output);
                Executors.newSingleThreadExecutor().submit(streamGobbler);
                int exitCode = proc.waitFor();*/

                String output = "teszt123";


                StringReader stringReader = new StringReader(output.toString());
                int c;
                while ((c = stringReader.read()) != -1) {
                    this.outputStream.write(c);
                    //System.out.println(c);
                }
                this.outputStream.flush();
                this.outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            System.out.println("-------------------------------");
        }

        try {
            this.outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuffer readData() {
        System.out.println("Reading data...");

        BufferedInputStream in = null;
        StringBuffer cmd = null;

        try {
            in = new BufferedInputStream(client.getInputStream());
            cmd = new StringBuffer();
            int c = -1;
            while ((c = in.read()) != -1) {
                cmd.append((char) c);
                if (c == (int) '\n') {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cmd;
    }
}
