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

    private void setActualImage(Mat actualImage) {
        this.actualImage = actualImage;
    }

    private void addToHistory(Mat matrix) {
        canvasHistory.addToHistory(matrix);
    }

    public void undo() {
        actualImage = canvasHistory.getPrevious();
    }

    public void redo() {
        actualImage = canvasHistory.getNext();
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
