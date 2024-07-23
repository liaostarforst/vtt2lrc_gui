package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BatchVtt2Lrc {
    public String inputFolder;
    public String outputFolder;
    private static int count = 0;
    private static final HashSet<String> SUFFIXS = new HashSet<String>() {{
        add("mp3");
        add("mp4");
        add("wav");
        add("flac");
//
    }};

    public void setInputFolder(String inputFolder){
        this.inputFolder = inputFolder;
    }
    public void setOutputFolder(String outputFolder){
        this.outputFolder = outputFolder;
    }

    public void startconvert(){
        System.out.println("指定的目录路径是: " + this.inputFolder);

        File topFolder = new File(this.inputFolder);

        boolean isSuccessful = convertVttFilesInFolder(topFolder, this.outputFolder);
    }

    private static boolean convertVttFilesInFolder(File folder, String outputFolder) {
        File[] files = folder.listFiles();

        if (files == null) {
            System.err.println("无法读取文件夹或文件夹为空。");
            return false;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // 如果是子文件夹，递归处理
                String subOutputFolder = outputFolder + File.separator + file.getName();
                convertVttFilesInFolder(file, subOutputFolder);
            } else if (file.getName().endsWith(".vtt")) {
                // 如果是VTT文件，进行转换
                convertVttToLrc(file, outputFolder);
            }
        }
        return true;
    }

    private static void convertVttToLrc(File vttFile, String outputFolder) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(vttFile), StandardCharsets.UTF_8))) {
            String lrcFileName = vttFile.getName().replace(".vtt", ".lrc");
            // handle the file name
            String[] split = lrcFileName.split("\\.");
            if (SUFFIXS.contains(split[split.length - 2])) {
                System.out.print("更改后的文件名："+lrcFileName);
                lrcFileName = lrcFileName.replace("." + split[split.length - 2], "");
                System.out.println("被重新命名为-----> "+lrcFileName);
            }

            File lrcFile = new File(outputFolder, lrcFileName);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lrcFile), StandardCharsets.UTF_8))) {
                String line;
                Pattern timePattern = Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{3}) --> (\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{3})");
                while ((line = reader.readLine()) != null) {
                    if (line.matches("\\d+")) {
                        // 如果行只包含数字，跳过该行（标识段落的数字）
                    } else if (line.matches("^\\s*WEBVTT(?=\\s*(?:\\d{2}:\\d{2}:\\d{2}.\\d{3}\\s*-->\\s*\\d{2}:\\d{2}:\\d{2}.\\d{3}\\s*)|$)")) {
                    } else if (line.contains("-->")) {
                        // 匹配VTT格式的时间戳，提取开始时间
                        Matcher matcher = timePattern.matcher(line);
                        if (matcher.find()) {
                            // 计算开始时间的总秒数
                            int startHours = Integer.parseInt(matcher.group(1));
                            int startMinutes = Integer.parseInt(matcher.group(2));
                            int startSeconds = Integer.parseInt(matcher.group(3));
                            int startMillis = Integer.parseInt(matcher.group(4));

                            int startTimeInSeconds = startHours * 3600 + startMinutes * 60 + startSeconds + startMillis / 1000;

                            // 将秒数格式化成 [mm:ss.SSS]
                            String currentTime = String.format("[%02d:%02d.%03d]", startTimeInSeconds / 60, startTimeInSeconds % 60, startMillis);

                            // 写入LRC格式的歌词行
                            writer.write(currentTime);

                        }
                    } else if (!line.isEmpty()) {
                        // 写入歌词内容
                        writer.write(line + "\n");
                    }
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
