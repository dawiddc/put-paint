package org.dawiddc.paint.controller.utils;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

public class ImageService {

    public ImageService() {
        nu.pattern.OpenCV.loadShared();
    }

    public Mat loadMatrix(String filePath) {
        return Imgcodecs.imread(filePath);
    }

    public Image convertToImage(Mat matrix) {

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", matrix, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
