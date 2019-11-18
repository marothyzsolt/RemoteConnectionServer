package com.topin;

import com.topin.services.ClientConnection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    /*public static class ImageCanvas extends Canvas {

        private BufferedImage img;


        public ImageCanvas() {
            try {
                Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                img = new Robot().createScreenCapture(screenSize);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", baos);
                baos.flush();
            } catch (IOException | AWTException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return img == null ? new Dimension(200, 200) : new Dimension(img.getWidth(), img.getHeight());
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (img != null) {
                int x = (getWidth() - img.getWidth()) / 2;
                int y = (getHeight() - img.getHeight()) / 2;
                g.drawImage(img, x, y, this);
            }
        }

    }*/

    public static void main(String[] args) {
        /*Canvas csStatusImage = new ImageCanvas();
        csStatusImage.setBounds(393, 36, 200, 200);

        Frame frame = new Frame("Testing");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.add(csStatusImage);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBounds(110, 110, 1024, 768);*/


        System.out.println("Starting server...");
        try {
            ServerSocket server = new ServerSocket(7777);
            while (true) {
                Socket client = server.accept();
                new Thread(new ClientConnection(client)).start();
                System.out.println("Client connected: " + client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
