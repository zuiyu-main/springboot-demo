package com.zuiyu.client.license;


import com.zuiyu.common.bean.LicenseVerifyParam;
import com.zuiyu.common.license.LicenseVerify;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 在项目启动时安装证书
 */
@Component
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {
    public static Logger log = LoggerFactory.getLogger(LicenseCheckListener.class);

    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass}")
    private String storePass;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath}")
    private String publicKeysStorePath;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // root application context 没有parent
        ApplicationContext context = event.getApplicationContext().getParent();
        if (context == null) {
            if (StringUtils.isNotBlank(licensePath)) {
                log.info("++++++++ 开始安装证书 ++++++++");

                LicenseVerifyParam param = new LicenseVerifyParam();
                param.setSubject(subject);
                param.setPublicAlias(publicAlias);
                param.setStorePass(storePass);
                param.setLicensePath(licensePath);
                param.setPublicKeysStorePath(System.getProperty("user.dir") + publicKeysStorePath);
                LicenseVerify licenseVerify = new LicenseVerify();
                //安装证书
                licenseVerify.install(param);

                log.info("++++++++ 证书安装结束 ++++++++");
            }
        }
    }
}
