package com.topin.model.command.help;

import com.topin.model.Message;
import org.json.JSONObject;

import java.io.File;

public class DriveUsageMessage extends Message {
    private File drive;
    private Long used;
    private Long all;

    public DriveUsageMessage(File drive, Long used, Long all) {
        super("driverUsage");
        this.drive = drive;
        this.used = used;
        this.all = all;
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put("type","driverUsage")
                .put("drive", this.drive)
                .put("used", this.used)
                .put("all", this.all)
                .toString();
    }

    public String toOutput() { return this.toString(); }
}