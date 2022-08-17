package com.zuiyu.server.controller;


import com.zuiyu.common.bean.LicenseCheckModel;
import com.zuiyu.common.bean.LicenseCreatorParam;
import com.zuiyu.common.license.AbstractServerInfos;
import com.zuiyu.common.license.LicenseCreator;
import com.zuiyu.common.license.LinuxServerInfos;
import com.zuiyu.common.license.WindowsServerInfos;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于生成证书文件，不能放在给客户部署的代码里
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/license")
public class LicenseCreatorController {
    public static Logger log = LoggerFactory.getLogger(LicenseCreatorController.class);


    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    @Value("${license.privateKeysStorePath}")
    private String privateKeysStorePath;

    /**
     * 获取服务器硬件信息
     *
     * @param osName 操作系统类型，如果为空则自动判断
     * @return com.ccx.models.license.LicenseCheckModel
     */
    @RequestMapping(value = "/getServerInfos", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName", required = false) String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }
        return abstractServerInfos.getServerInfos();
    }

    /**
     * 生成证书
     *
     * @param param 生成证书需要的参数，
     *              {
     *              "subject": "zuiyu_demo",
     *              "privateAlias": "zuiyuPrivateKey",
     *              "keyPass": "zuiyu_private_password_1234",
     *              "storePass": "zuiyu_public_password_1234",
     *              "licensePath": "/Users/cxt/Downloads/license/license7.lic",
     *              "privateKeysStorePath": "/Users/cxt/Downloads/license/privateKeys.keystore",
     *              "issuedTime": "2018-07-10 00:00:01",
     *              "expiryTime": "2022-12-31 23:59:59",
     *              "consumerType": "User",
     *              "consumerAmount": 1,
     *              "description": "这是证书描述信息",
     *              "licenseCheckModel": {
     *              "checkIp":false,
     *              "ipAddress": [""],
     *              "checkMac":true,
     *              "macAddress": ["aa-bb-cc-11"],
     *              "checkCpu":false,
     *              "cpuSerial": "",
     *              "checkMainBoard":false,
     *              "mainBoardSerial": ""
     *              }
     *              }
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String, Object> generateLicense(HttpServletRequest request, @RequestBody LicenseCreatorParam param) {
        Map<String, Object> resultMap = new HashMap<>(2);

        if (StringUtils.isBlank(param.getLicensePath())) {
            param.setLicensePath(licensePath);
        }

        String path = System.getProperty("user.dir") + privateKeysStorePath;
        param.setPrivateKeysStorePath(path);
        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();

        if (result) {
            resultMap.put("result", "ok");
            resultMap.put("msg", param);
        } else {
            resultMap.put("result", "error");
            resultMap.put("msg", "证书文件生成失败！");
        }
        return resultMap;
    }
}
