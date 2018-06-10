package org.dawiddc.paint.model;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class Canvas {
    private Mat actualImage;
    private CanvasHistory canvasHistory;

    public Canvas(Mat actualImage) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        this.canvasHistory = new CanvasHistory();
        updateHistory(actualImage);
    }

    public CanvasHistory getCanvasHistory() {
        return canvasHistory;
    }

    public Mat getActualImage() {
        return actualImage;
    }

    public void setActualImage(Mat actualImage) {
        this.actualImage = actualImage;
    }

    public void addToHistory(Mat matrix) {
        canvasHistory.addToHistory(matrix);
    }

    public Mat undo() {
        actualImage = canvasHistory.getPrevious();
        return actualImage;
    }

    public Mat redo() {
        actualImage = canvasHistory.getNext();
        return actualImage;
    }

    public boolean isHistoryEmpty() {
        return canvasHistory.isEmpty();
    }

    public void updateHistory(Mat matrix) {
        if (!canvasHistory.isIteratorAtLast()) {
            canvasHistory.deleteAfterIterator();
        }
        setActualImage(matrix);
        addToHistory(actualImage);
    }
}
