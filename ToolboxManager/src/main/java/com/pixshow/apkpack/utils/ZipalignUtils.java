package com.pixshow.apkpack.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

public class ZipalignUtils {

    public static boolean exe(File in, File out) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        File executableFile = new File(ZipalignUtils.class.getResource("/sbin/" + (isWindows() ? "zipalign.exe" : "zipalign")).toURI());
        map.put("exefile", executableFile);
        map.put("infile", in);
        map.put("outfile", out);
        CommandLine cmdLine = null;
        if (isWindows()) {
            cmdLine = CommandLine.parse("cmd.exe /C ${exefile} -f -v 4 ${infile} ${outfile}", map);
        } else {
            cmdLine = CommandLine.parse("${exefile} -f -v 4 ${infile} ${outfile}", map);
        }
        DefaultExecutor executor = new DefaultExecutor();
        int exitValue = executor.execute(cmdLine);
        return exitValue == 0 ? true : false;
    }

    public static boolean isWindows() {
        String name = System.getProperty("os.name").toLowerCase();
        return name.indexOf("windows") >= 0;
    }

    public static void main(String[] args) {
        try {
            String root = "C:/Users/JFL/Desktop/";
            exe(new File(root + "Calculators_GP_9_googleplay.apk"), new File(root + "Calculators_GP_9_googleplay_a1.apk"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
