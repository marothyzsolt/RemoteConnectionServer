package com.topin.model.command;

import com.topin.model.Message;
import org.json.JSONObject;

public class InitMessage extends Message {
    private String hostname;
    private String cpuName;
    private String localIp;
    private String osName;
    private String osVersion;
    private String biosVersion;
    private Float cpuUsage;
    private Integer ramMax;
    private Integer ramUsage;
    private String driveUsage; // Json: C: 11GB/256GB  USED/ALL

    private String taskList; // JSON: {count: 121, [{name: 'Total Commander', status: 'idle', 'pid': 12331}]}

    public InitMessage(String type, String hostname, String cpuName, String localIp, String osName, String osVersion, String biosVersion, Float cpuUsage, Integer ramMax, Integer ramUsage, String driveUsage, String taskList) {
        super(type);
        this.hostname = hostname;
        this.cpuName = cpuName;
        this.localIp = localIp;
        this.osName = osName;
        this.osVersion = osVersion;
        this.biosVersion = biosVersion;
        this.cpuUsage = cpuUsage;
        this.ramMax = ramMax;
        this.ramUsage = ramUsage;
        this.driveUsage = driveUsage;
        this.taskList = taskList;
    }


    @Override
    public String toJson() {
        return new JSONObject()
                .put("type", "initMessage")
                .put("success", this.hostname)
                .put("cpuName", this.cpuName)
                .put("localIp", this.localIp)
                .put("osName", this.osName)
                .put("osVersion", this.osVersion)
                .put("biosVersion", this.biosVersion)
                .put("cpuUsage", this.cpuUsage)
                .put("ramMax", this.ramMax)
                .put("ramUsage", this.ramUsage)
                .put("driveUsage", this.driveUsage)
                .put("taskList", this.taskList)
                .toString();
    }
}
