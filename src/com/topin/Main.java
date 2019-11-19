package com.topin;

import com.topin.model.builder.MessageBuilder;
import com.topin.services.ClientConnection;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    /*public static class ImageCanvas extends Canvas {

        private BufferedImage img;

        public String captureToBase64() {

            Rectangle screenSize = new
                    Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenCapture = null;
            String base64Encoded = "";

            try {

                screenCapture = new Robot().createScreenCapture(screenSize);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(screenCapture, "jpg", baos);
                baos.flush();
                byte[] encodeBase64 = Base64.getEncoder().encode(baos.toByteArray());
                base64Encoded = new String(encodeBase64);
                baos.close();

            } catch (AWTException | IOException e) {
                e.getMessage();
            }


            return base64Encoded;
        }

        public ImageCanvas() {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                String b64 = this.captureToBase64();
                InputStream in = new ByteArrayInputStream(
                        Base64.getDecoder().decode(b64)
                );
                img = ImageIO.read(in);

                ImageIO.write(img, "jpg", baos);
                baos.flush();
            } catch (IOException ex) {
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


        /*String jsonData = new MessageBuilder("command").add("command", "cmd.exe /c dir").get().toString();
        JSONObject jsonObject = new JSONObject(jsonData);
        System.out.println(jsonObject.get("command"));
        System.exit(0);*/


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
