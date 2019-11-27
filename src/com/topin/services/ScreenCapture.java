package com.topin.services;

import com.topin.driver.ClientMessageDriver;
import com.topin.model.command.StatusMessage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ScreenCapture implements Runnable {

    private ClientMessageDriver clientMessageDriver;
    private String lastCapture;

    public ScreenCapture(ClientMessageDriver clientMessageDriver) {
        this.clientMessageDriver = clientMessageDriver;
    }

    public void run() {
        /*while(true) {
            String captured = this.captureToBase64();
            if (!captured.equals(this.lastCapture)) {
                clientMessageDriver.send(captured);
                this.lastCapture = captured;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }*/

        while(true) {
            String message = new StatusMessage(true, "").toJson();
            clientMessageDriver.send(message);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {}
        }
    }


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
}
