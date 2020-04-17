package com.tz.fdfs.config;

import net.anumbrella.seaweedfs.core.Connection;
import net.anumbrella.seaweedfs.core.FileTemplate;
import net.anumbrella.seaweedfs.core.MasterWrapper;
import net.anumbrella.seaweedfs.core.VolumeWrapper;
import net.anumbrella.seaweedfs.core.content.AssignFileKeyParams;
import net.anumbrella.seaweedfs.core.content.LocationResult;
import net.anumbrella.seaweedfs.core.content.LookupVolumeParams;
import net.anumbrella.seaweedfs.core.content.LookupVolumeResult;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import net.anumbrella.seaweedfs.core.http.HeaderResponse;
import net.anumbrella.seaweedfs.exception.SeaweedfsException;
import net.anumbrella.seaweedfs.exception.SeaweedfsFileDeleteException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author https://github.com/TianPuJun @无痕
 * @ClassName MyFileTemplate
 * @Description
 * @Date 10:06 2020/3/12
 **/
public class MyFileTemplate implements InitializingBean, DisposableBean {
    private static final Log log = LogFactory.getLog(FileTemplate.class);
    private static final SimpleDateFormat headerDateFormat =
            new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);


    private MasterWrapper masterWrapper;
    private MyVolumeWrapper myVolumeWrapper;
    private VolumeWrapper volumeWrapper;

    private int sameRackCount = 0;
    private int diffRackCount = 0;
    private int diffDataCenterCount = 0;
    private String replicationFlag = "000";
    private String timeToLive = null;
    private String dataCenter = null;
    private String collection = null;
    private boolean usingPublicUrl = true;
    private boolean loadBalance = true;
    private AssignFileKeyParams assignFileKeyParams = new AssignFileKeyParams();

    /**
     * Constructor.
     *
     * @param connection Connection from file source.
     */
    public MyFileTemplate(Connection connection) {
        this.masterWrapper = new MasterWrapper(connection);
        this.myVolumeWrapper = new MyVolumeWrapper(connection);
        this.volumeWrapper = new VolumeWrapper(connection);
        headerDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    /**
     * Get file status without file stream.
     *
     * @param fileId File id.
     * @return File status.
     * @throws IOException Http connection is fail or server response within some error message.
     */
    public FileHandleStatus getFileStatus(String fileId, String fileName, String contentType) throws IOException {
        final String targetUrl = getTargetUrl(fileId);
        HeaderResponse headerResponse = volumeWrapper.getFileStatusHeader(targetUrl, fileId);
        try {
            return new FileHandleStatus(fileId,
                    headerDateFormat.parse(headerResponse.getLastHeader("Last-Modified").getValue()).getTime(),
                    fileName,
                    //"application/pdf"
                    contentType,
                    Long.parseLong(headerResponse.getLastHeader("Content-Length").getValue()));
        } catch (ParseException e) {
            throw new SeaweedfsException("Could not parse last modified time [" +
                    headerResponse.getLastHeader("Last-Modified").getValue() + "] to long value");
        }
    }


    private String getTargetUrl(String fileId) throws IOException {
        if (usingPublicUrl) {
            return getTargetLocation(fileId).getPublicUrl();
        } else {
            return getTargetLocation(fileId).getUrl();
        }

    }

    private LocationResult getTargetLocation(String fileId) throws IOException {
        final LookupVolumeResult volumeResult = masterWrapper.lookupVolume(new LookupVolumeParams(fileId, collection));
        if (volumeResult.getLocations() == null || volumeResult.getLocations().size() == 0) {
            throw new SeaweedfsFileDeleteException(fileId,
                    new SeaweedfsException("can not found the volume server"));
        }
        return volumeResult.getLocations().iterator().next();
    }

    @Override
    public void destroy() throws Exception {
        this.masterWrapper = null;
        this.volumeWrapper = null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
