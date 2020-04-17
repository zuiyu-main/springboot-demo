package com.tz.fdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName FileUtil
 * @Description
 * @Date 16:50 2020/2/16
 **/
public class FileUtil {
    public static String uploadImage(MultipartFile multipartFile) {
        String imgUrl = "http://192.168.1.91";
        // 上传图片到服务器
        // 配置fdfs的全局链接地址
        String tracker = FileUtil.class.getResource("fdfs_client.conf").getPath();// 获得配置文件的路径
        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrackerClient trackerClient = new TrackerClient();
        // 获得一个trackerServer的实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通过tracker获得一个Storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            byte[] bytes = multipartFile.getBytes();// 获得上传的二进制对象
            // 获得文件后缀名
            String originalFilename = multipartFile.getOriginalFilename();// a.jpg
            System.out.println(originalFilename);
            int i = originalFilename.lastIndexOf(".");
            String extName = originalFilename.substring(i + 1);
            String[] uploadInfos = storageClient.upload_file(bytes, extName, null);
            for (String uploadInfo : uploadInfos) {
                imgUrl += "/" + uploadInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUrl;
    }


    private static TrackerClient trackerClient = null;
    private static TrackerServer trackerServer = null;
    private static StorageServer storageServer = null;
    private static StorageClient storageClient = null;

    static {
        try {
            ClientGlobal.init("fdfs_client.conf");
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getTrackerServer();
            storageClient = new StorageClient(trackerServer, storageServer);
        } catch (IOException | MyException e) {
            throw new RuntimeException("FastDfs工具类初始化失败!");
        }
    }

    /**
     * @param @param  inputStream 文件流
     * @param @param  filename 文件名称
     * @param @return
     * @param @throws IOException
     * @param @throws MyException
     * @return String 返回文件在FastDfs的存储路径
     * @throws
     * @Title: fdfsUpload
     * @Description: 通过文件流上传文件
     */
    public String fdfsUpload(InputStream inputStream, String filename) throws IOException, MyException {
        String suffix = ""; //后缀名
        try {
            suffix = filename.substring(filename.lastIndexOf(".") + 1);
        } catch (Exception e) {
            throw new RuntimeException("参数filename不正确!格式例如：a.png");
        }
        String savepath = ""; //FastDfs的存储路径
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buff)) != -1) {
            swapStream.write(buff, 0, len);
        }
        byte[] in2b = swapStream.toByteArray();
        String[] strings = storageClient.upload_file(in2b, suffix, null); //上传文件
        for (String str : strings) {
            savepath += "/" + str; //拼接路径
        }
        return savepath;
    }

    /**
     * @param @param  filepath 本地文件路径
     * @param @return
     * @param @throws IOException
     * @param @throws MyException
     * @return String 返回文件在FastDfs的存储路径
     * @throws
     * @Title: fdfsUpload
     * @Description: 本地文件上传
     */
    public static String fdfsUpload(String filepath) throws IOException, MyException {
        String suffix = ""; //后缀名
        try {
            suffix = filepath.substring(filepath.lastIndexOf(".") + 1);
        } catch (Exception e) {
            throw new RuntimeException("上传的不是文件!");
        }
        String savepath = ""; //FastDfs的存储路径
        //String[] strings = storageClient.upload_file(filepath.getBytes(), suffix, null);
        String[] strings = storageClient.upload_file(filepath, suffix, null); //上传文件
        for (String str : strings) {
            savepath += "/" + str; //拼接路径
        }
        return savepath;
    }

    /**
     * @param @param  savepath 文件存储路径
     * @param @param  localPath 下载目录
     * @param @return
     * @param @throws IOException
     * @param @throws MyException
     * @return boolean 返回是否下载成功
     * @throws
     * @Title: fdfsDownload
     * @Description: 下载文件到目录
     */
    public static boolean fdfsDownload(String savepath, String localPath) throws IOException, MyException {
        String group = ""; //存储组
        String path = ""; //存储路径
        try {
            int secondindex = savepath.indexOf("/", 2); //第二个"/"索引位置
            group = savepath.substring(1, secondindex); //类似：group1
            path = savepath.substring(secondindex + 1); //类似：M00/00/00/wKgBaFv9Ad-Abep_AAUtbU7xcws013.png
        } catch (Exception e) {
            throw new RuntimeException("传入文件存储路径不正确!格式例如：/group1/M00/00/00/wKgBaFv9Ad-Abep_AAUtbU7xcws013.png");
        }
        int result = storageClient.download_file(group, path, localPath);
        if (result != 0) {
            throw new RuntimeException("下载文件失败：文件路径不对或者文件已删除!");
        }
        return true;
    }

    /**
     * @param @param  savepath 文件存储路径
     * @param @return
     * @param @throws IOException
     * @param @throws MyException
     * @return byte[] 字符数组
     * @throws
     * @Title: fdfsDownload
     * @Description: 返回文件字符数组
     */
    public static byte[] fdfsDownload(String savepath) throws IOException, MyException {
        byte[] bs = null;
        String group = ""; //存储组
        String path = ""; //存储路径
        try {
            int secondindex = savepath.indexOf("/", 2); //第二个"/"索引位置
            group = savepath.substring(1, secondindex); //类似：group1
            path = savepath.substring(secondindex + 1); //类似：M00/00/00/wKgBaFv9Ad-Abep_AAUtbU7xcws013.png
        } catch (Exception e) {
            throw new RuntimeException("传入文件存储路径不正确!格式例如：/group1/M00/00/00/wKgBaFv9Ad-Abep_AAUtbU7xcws013.png");
        }
        bs = storageClient.download_file(group, path); //返回byte数组
        return bs;
    }

    /**
     * @param @param  savepath 文件存储路径
     * @param @return
     * @param @throws IOException
     * @param @throws MyException
     * @return boolean 返回true表示删除成功
     * @throws
     * @Title: fdfsDeleteFile
     * @Description: 删除文件
     */
    public static boolean fdfsDeleteFile(String savepath) throws IOException, MyException {
        String group = ""; //存储组
        String path = ""; //存储路径
        try {
            int secondindex = savepath.indexOf("/", 2); //第二个"/"索引位置
            group = savepath.substring(1, secondindex); //类似：group1
            path = savepath.substring(secondindex + 1); //类似：M00/00/00/wKgBaFv9Ad-Abep_AAUtbU7xcws013.png
        } catch (Exception e) {
            throw new RuntimeException("传入文件存储路径不正确!格式例如：/group1/M00/00/00/wKgBaFv9Ad-Abep_AAUtbU7xcws013.png");
        }
        int result = storageClient.delete_file(group, path); //删除文件，0表示删除成功
        if (result != 0) {
            throw new RuntimeException("删除文件失败：文件路径不对或者文件已删除!");
        }
        return true;
    }

    /**
     * @param @param  savepath 文件存储路径
     * @param @return
     * @param @throws IOException
     * @param @throws MyException
     * @return FileInfo 文件信息
     * @throws
     * @Title: fdfdFileInfo
     * @Description: 返回文件信息
     */
    public static FileInfo fdfdFileInfo(String savepath) throws IOException, MyException {
        String group = ""; //存储组
        String path = ""; //存储路径
        try {
            int secondindex = savepath.indexOf("/", 2); //第二个"/"索引位置
            group = savepath.substring(1, secondindex); //类似：group1
            path = savepath.substring(secondindex + 1); //类似：M00/00/00/wKgBaFv9Ad-Abep_AAUtbU7xcws013.png
        } catch (Exception e) {
            throw new RuntimeException("传入文件存储路径不正确!格式例如：/group1/M00/00/00/wKgBaFv9Ad-Abep_AAUtbU7xcws013.png");
        }
        FileInfo fileInfo = storageClient.get_file_info(group, path);
        return fileInfo;
    }


    public static void main(String[] args) {
        FileUtil fast = new FileUtil();
        try {
            String str = "shenningwuid";
            InputStream stream = new ByteArrayInputStream(str.getBytes());
            String txt = fast.fdfsUpload(stream, "txt");
            System.out.println(txt);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
}
