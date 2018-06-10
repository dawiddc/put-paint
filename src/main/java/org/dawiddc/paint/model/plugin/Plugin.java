package org.dawiddc.paint.model.plugin;

import org.opencv.core.Mat;

public abstract class Plugin {

    private String name;

    public Plugin(String name) {
        this.name = name;
    }

    public abstract Mat run(Mat image);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
