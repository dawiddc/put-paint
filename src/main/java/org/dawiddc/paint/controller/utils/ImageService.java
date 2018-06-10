package org.dawiddc.paint.controller.utils;

import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

public class ImageService {


    public ImageService() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public Mat loadMatrix(String filePath) {
        Imgcodecs imageCodecs = new Imgcodecs();
        Mat matrix = Imgcodecs.imread(filePath);
        return matrix;
    }

    public Image convertToImage(Mat matrix) {

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", matrix, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
