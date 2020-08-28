package com.tz.fdfs.util;

import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName Md5Utils
 * @Description
 * @Date 17:31 2020/8/21
 **/
public class Md5Utils {


    public static String getMD5(File file) throws IOException {
        return DigestUtils.md5DigestAsHex(new FileInputStream(file));
    }

    public static String getMD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

}
