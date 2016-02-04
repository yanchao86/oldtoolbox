package com.pixshow.apkpack.utils;

import java.util.LinkedList;
import java.util.List;

public class CollectingLogOutputStream extends LogOutputStream {

    public CollectingLogOutputStream(String charsetName) {
        super(charsetName);
    }

    private final List<String> lines = new LinkedList<String>();

    @Override
    protected void processLine(String line, int level) {
        lines.add(line);
    }

    public List<String> getLines() {
        return lines;
    }
}
