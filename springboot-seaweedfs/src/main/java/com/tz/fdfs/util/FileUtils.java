package com.tz.fdfs.util;

import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName FileUtils
 * @Description 文件工具类
 * @Date 17:54 2020/8/21
 **/
public class FileUtils {

    public static MultipartFile file2MultipartFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
        return multipartFile;
    }

    public static void multipartFile2File(MultipartFile file, File targetFile) throws IOException {
        org.apache.commons.io.FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
    }

    /**
     *                 DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem("file",
     *                         MediaType.IMAGE_PNG_VALUE, true, filename);
     *                 try (OutputStream outputStream = fileItem.getOutputStream();) {
     *                     ImageIO.write(image, "jpg", outputStream);
     *                 } catch (IOException e) {
     *                 }
     *                 MultipartFile multi = new CommonsMultipartFile(fileItem);
     */
    /**
     * 文件分割方法
     *
     * @param targetFile 分割的文件
     * @param cutSize    分割文件的大小
     * @return int 文件切割的个数
     */
    public static int getSplitFile(File targetFile, long cutSize) {

        //计算切割文件大小
        int count = targetFile.length() % cutSize == 0 ? (int) (targetFile.length() / cutSize) :
                (int) (targetFile.length() / cutSize + 1);

        RandomAccessFile raf = null;
        try {
            //获取目标文件 预分配文件所占的空间 在磁盘中创建一个指定大小的文件   r 是只读
            raf = new RandomAccessFile(targetFile, "r");
            long length = raf.length();//文件的总长度
            long maxSize = length / count;//文件切片后的长度
            long offSet = 0L;//初始化偏移量

            for (int i = 0; i < count - 1; i++) { //最后一片单独处理
                long begin = offSet;
                long end = (i + 1) * maxSize;
                offSet = getWrite(targetFile.getAbsolutePath(), i, begin, end);
            }
            if (length - offSet > 0) {
                getWrite(targetFile.getAbsolutePath(), count - 1, offSet, length);
            }

        } catch (FileNotFoundException e) {
//            System.out.println("没有找到文件");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 指定文件每一份的边界，写入不同文件中
     *
     * @param file  源文件地址
     * @param index 源文件的顺序标识
     * @param begin 开始指针的位置
     * @param end   结束指针的位置
     * @return long
     */
    public static long getWrite(String file, int index, long begin, long end) {

        long endPointer = 0L;

        String a = file.split(suffixName(new File(file)))[0];

        try {
            //申明文件切割后的文件磁盘
            RandomAccessFile in = new RandomAccessFile(new File(file), "r");
            //定义一个可读，可写的文件并且后缀名为.tmp的二进制文件
            //读取切片文件
            File mFile = new File(a + "_" + index + ".tmp");
            System.out.println(mFile.getAbsolutePath());
            //如果存在
            if (!mFile.exists()) {
                RandomAccessFile out = new RandomAccessFile(mFile, "rw");
                //申明具体每一文件的字节数组
                byte[] b = new byte[1024];
                int n = 0;
                //从指定位置读取文件字节流
                in.seek(begin);
                //判断文件流读取的边界
                while ((n = in.read(b)) != -1 && in.getFilePointer() <= end) {
                    //从指定每一份文件的范围，写入不同的文件
                    out.write(b, 0, n);
                }

                //定义当前读取文件的指针
                endPointer = in.getFilePointer();
                //关闭输入流
                in.close();
                //关闭输出流
                out.close();
            } else {
                //不存在
                System.out.println("不存在");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return endPointer - 1024;
    }

    /**
     * 获取文件后缀名 例如：.mp4 /.jpg /.apk
     *
     * @param file 指定文件
     * @return String 文件后缀名
     */
    public static String suffixName(File file) {
        String fileName = file.getName();
        String fileTyle = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        return fileTyle;
    }

    /**
     * 文件合并
     *
     * @param fileName   指定合并文件
     * @param targetFile 分割前的文件
     * @param cutSize    分割文件的大小
     */
    public static void merge(String fileName, File targetFile, long cutSize) {


        int tempCount = targetFile.length() % cutSize == 0 ? (int) (targetFile.length() / cutSize) :
                (int) (targetFile.length() / cutSize + 1);
        //文件名
        String a = targetFile.getAbsolutePath().split(suffixName(targetFile))[0];

        RandomAccessFile raf = null;
        try {
            //申明随机读取文件RandomAccessFile
            raf = new RandomAccessFile(new File(fileName + suffixName(targetFile)), "rw");
            //开始合并文件，对应切片的二进制文件
            for (int i = 0; i < tempCount; i++) {
                //读取切片文件
                File mFile = new File(a + "_" + i + ".tmp");
                //
                RandomAccessFile reader = new RandomAccessFile(mFile, "r");
                byte[] b = new byte[1024];
                int n = 0;
                //先读后写
                while ((n = reader.read(b)) != -1) {//读
                    raf.write(b, 0, n);//写
                }
                //合并后删除文件
//                isDeleteFile(mFile);
                //日志
//                log(mFile.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        File file = new File("/Users/cxt/Downloads/test2/重新定义Spring Cloud实战-1.pdf");

        int splitFile = getSplitFile(file, 1024 * 1024 * 5);

        System.out.println(splitFile);
    }

}
