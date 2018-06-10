package org.dawiddc.paint.model;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.LinkedList;
import java.util.List;

public class CanvasHistory {


    int iterator;
    private LinkedList<Mat> history;

    public CanvasHistory() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        this.history = new LinkedList<>();
        setIterator(-1);
    }

    public int getIterator() {
        return iterator;
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public List<Mat> getHistory() {
        return history;
    }

    public void addToHistory(Mat matrix) {
        history.add(matrix);
        iterator++;
    }

    public Mat getLast() {
        iterator = history.size() - 1;
        return history.getLast();
    }

    public Mat getNext() {
        if (iterator + 1 < history.size()) {
            iterator++;
        }
        return history.get(iterator);
    }

    public Mat getPrevious() {
        if (iterator - 1 >= 0) {
            iterator--;
        }
        return history.get(iterator);
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public boolean isIteratorAtLast() {
        return getIterator() == history.size() - 1;
    }

    public void deleteAfterIterator() {
        while (getIterator() + 1 < history.size()) {
            history.pollLast();
        }
    }
}
