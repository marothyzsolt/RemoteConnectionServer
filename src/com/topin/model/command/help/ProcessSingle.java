package com.topin.model.command.help;

public class ProcessSingle {
    private Integer pid;
    private String name;
    private String memoryUsage;

    public ProcessSingle(Integer pid, String name, String memoryUsage) {
        this.pid = pid;
        this.name = name;
        this.memoryUsage = memoryUsage;
    }

    public Integer getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getMemoryUsage() {
        return memoryUsage;
    }
}
