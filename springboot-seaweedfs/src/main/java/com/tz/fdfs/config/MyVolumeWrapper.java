package com.tz.fdfs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.anumbrella.seaweedfs.core.Connection;
import net.anumbrella.seaweedfs.core.http.HeaderResponse;
import net.anumbrella.seaweedfs.core.http.JsonResponse;
import net.anumbrella.seaweedfs.exception.SeaweedfsException;
import net.anumbrella.seaweedfs.exception.SeaweedfsFileNotFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.CharsetUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author tz
 * @ClassName MyVolumeWrapper
 * @Description 自定义服务，重写VolumeWrapper
 * @Date 15:37 2020/1/15
 **/
@Slf4j
public class MyVolumeWrapper {

    private Connection connection;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor.
     *
     * @param Connection Connection from file source.
     */
    public MyVolumeWrapper(Connection Connection) {
        this.connection = Connection;
    }


    /**
     * 添加了拼接json文件合并，?cm=true
     * https://github.com/chrislusf/seaweedfs/wiki/Large-File-Handling
     *
     * @param url
     * @param fid
     * @param fileName
     * @param stream
     * @param ttl
     * @param contentType
     * @return
     * @throws Exception
     */
    public long uploadFileWithCm(String url, String fid, String fileName, InputStream stream, String ttl, ContentType contentType) throws Exception {
        HttpPost httpPost;
        if (ttl != null) {
            httpPost = new HttpPost(url + "/" + fid + "?ttl=" + ttl);
        } else {
            httpPost = new HttpPost(url + "/" + fid + "?cm=true&pretty=yes");
        }

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        httpPost.setHeader(new BasicHeader("Accept-Language", "zh-cn"));

        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(CharsetUtils.get("UTF-8"));
        builder.addBinaryBody("upload", stream, contentType, fileName);
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        JsonResponse jsonResponse = connection.fetchJsonResultByRequest(httpPost);
        //如果jsonResponse为空,只有可能这个文件比较大
        if (jsonResponse == null) {
            log.error("MyVolumeWrapper 上传文件失败，原因1：可能文件较大，获取服务器连接超时。jsonResponse：[null]");
            jsonResponse = new JsonResponse("{\"name\":\"" + fileName + "\",\"size\":0}", HttpStatus.SC_OK);
        }
        convertResponseStatusToException(jsonResponse.statusCode, url, fid, false, false, false, false);
        return (Integer) objectMapper.readValue(jsonResponse.json, Map.class).get("size");
    }

    /**
     * @param statusCode         Server response code.
     * @param url                Server url.
     * @param fid                File id.
     * @param ignoreNotFound     Ignore http response not found status.
     * @param ignoreRedirect     Ignore http response redirect status.
     * @param ignoreRequestError Ignore http response request error status.
     * @param ignoreServerError  Ignore http response server error status.
     * @throws SeaweedfsException Http connection is fail or server response within some error message.
     */
    private void convertResponseStatusToException(int statusCode, String url, String fid,
                                                  boolean ignoreNotFound,
                                                  boolean ignoreRedirect,
                                                  boolean ignoreRequestError,
                                                  boolean ignoreServerError) throws SeaweedfsException {

        switch (statusCode / 100) {
            case 1:
                return;
            case 2:
                return;
            case 3:
                if (ignoreRedirect) {
                    return;
                }
                throw new SeaweedfsException(
                        "fetch file from [" + url + "/" + fid + "] is redirect, " +
                                "response stats code is [" + statusCode + "]");
            case 4:
                if (statusCode == 404 && ignoreNotFound) {
                    return;
                } else if (statusCode == 404) {
                    throw new SeaweedfsFileNotFoundException(
                            "fetch file from [" + url + "/" + fid + "] is not found, " +
                                    "response stats code is [" + statusCode + "]");
                }
                if (ignoreRequestError) {
                    return;
                }
                throw new SeaweedfsException(
                        "fetch file from [" + url + "/" + fid + "] is request error, " +
                                "response stats code is [" + statusCode + "]");
            case 5:
                if (ignoreServerError) {
                    return;
                }
                throw new SeaweedfsException(
                        "fetch file from [" + url + "/" + fid + "] is request error, " +
                                "response stats code is [" + statusCode + "]");
            default:
                throw new SeaweedfsException(
                        "fetch file from [" + url + "/" + fid + "] is error, " +
                                "response stats code is [" + statusCode + "]");
        }
    }

    /**
     * Get file status.
     *
     * @param url Server url.
     * @param fid File id.
     * @return File status header.
     * @throws IOException Http connection is fail or server response within some error message.
     */
    public HeaderResponse getFileStatusHeader(String url, String fid) throws IOException {
        HttpHead request = new HttpHead(url + "/" + fid);
        HeaderResponse cache = connection.fetchHeaderByRequest(request);
        convertResponseStatusToException(cache.getHttpResponseStatusCode(), url, fid, false, false, false, false);
        return cache;
    }
}
