package com.tz.fdfs.util;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName HtmlParse
 * @Description
 * @Date 15:32 2020/7/9
 **/
public class HtmlParse {

    public static String getEncode(String filePath) throws IOException {

        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);

        CharsetDetector detector = new CharsetDetector();
        detector.setText(data);
        CharsetMatch match = detector.detect();
        String encoding = match.getName();
        System.out.println("The Content in " + match.getName());
        return encoding;
    }

    public static void main(String[] args) throws Exception {

        String path = "/Users/cxt/Downloads/最高人民法院办公厅关于配合互联网阅卷开展诉讼案卷审查工作情况的通报(法办【2015】122号).html";
        String encode = HtmlParse.getEncode(path);
        StringBuilder str = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(path)), encode);
            int c = 0;
            while ((c = reader.read()) != -1) {
                str.append((char) c);
            }
            reader.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Document parse = Jsoup.parse(str.toString());
        System.out.println(parse.text());
        String text = parse.text();
        String substring = text.substring(text.indexOf("：") + 1);
        System.out.println(substring);
    }
}
