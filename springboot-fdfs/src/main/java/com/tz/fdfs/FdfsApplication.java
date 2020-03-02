package com.tz.fdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class FdfsApplication {

	public static void main(String[] args) throws IOException, MyException {
		ClientGlobal.init("fdfs_client.conf");
		ClientGlobal.initByProperties("fastdfs-client.properties");
		Properties props = new Properties();
		props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, "127.0.0.1:22122");
		ClientGlobal.initByProperties(props);
		String trackerServers = "192.168.1.91:22122";
		ClientGlobal.initByTrackers(trackerServers);
		System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());

		SpringApplication.run(FdfsApplication.class, args);
	}

}
