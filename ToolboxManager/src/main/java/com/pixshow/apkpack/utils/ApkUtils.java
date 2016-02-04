package com.pixshow.apkpack.utils;


public class ApkUtils {

    public static void decode(String apkFileName, String outDir) throws Exception {
        // String cmd = "apktool d -f " + apkFileName + " -o " + outDir;
        // CommandLine cmdLine = CommandLine.parse(cmd);
        // DefaultExecutor executor = new DefaultExecutor();
        // int exitValue = executor.execute(cmdLine);
        // if (exitValue != 0) { throw new RuntimeException("执行失败" + cmd); }
        brut.apktool.Main.main(new String[] { "d", "-f", apkFileName, outDir });
    }

    public static void build(String appDirName, String outFile) throws Exception {
        // String cmd = "apktool b -f " + appDirName + " -o " + outFile;
        // CommandLine cmdLine = CommandLine.parse(cmd);
        // DefaultExecutor executor = new DefaultExecutor();
        // int exitValue = executor.execute(cmdLine);
        // if (exitValue != 0) { throw new RuntimeException("执行失败" + cmd); }
        brut.apktool.Main.main(new String[] { "b", "-f", appDirName, outFile });
    }
}
