# 工程简介

授权生成服务

# 延伸阅读

# 使用keytool工具生成签名文件

> https://mp.weixin.qq.com/s?__biz=MzIwNzYzODIxMw==&mid=2247485388&idx=1&sn=dd51ac189b41c02a65f6df4577f481d4&chksm=970e1c66a0799570e8c7391ee5e9a3fd2fb09a29d05e3200671fed8d9f52c385d01671090c97#rd

## 生成私钥公钥

* 使用JDK自带的`keytool`工具生成签名

  ```text
  keytool -genkeypair -keysize 1024 -validity 3650 -alias "zuiyuPrivateKey" -keypass "zuiyu_private_password_1234" -keystore "zuiyuPrivateKeys.keystore" -storepass "zuiyu_public_password_1234" -dname "CN=zuiyu,OU=zuiyu,O=zuiyu,L=BJ,ST=BJ,C=CN"
  ```


* 导出签名文件 zuiyuCertfile.cer

  ```text
  keytool -exportcert -alias "zuiyuPrivateKey" -keystore "zuiyuPrivateKeys.keystore" -storepass "zuiyu_public_password_1234" -file "zuiyuCertfile.cer"
  ```

* 导入签名文件

  ```text
  keytool -import -alias "zuiyuPublicCert" -file "zuiyuCertfile.cer" -keystore "zuiyuPublicCerts.keystore" -storepass "zuiyu_public_password_1234"
  ```

* 帮助命令（根据需要食用）

  ```text
  # 删除
  keytool -delete -alias zuiyuPrivateKey -keystore "zuiyuPrivateKeys.keystore" -storepass "zuiyu_public_password_1234"
  
  # 查看
  keytool -list -v -keystore zuiyuPrivateKeys.keystore -storepass "zuiyu_public_password_1234"
  ```

* 最后

  上述命令执行完成之后，会在当前路径下生成三个文件，分别是：`zuiyuPrivateKeys.keystore`、`zuiyuPublicCerts.keystore`、`zuiyuCertfile.cer`
  。其中文件`zuiyuCertfile.cer`不再需要可以删除，文件`zuiyuPrivateKeys.keystore`用于当前的` license-server `项目给客户生成`license`
  文件，而文件`zuiyuPublicCerts.keystore`则随应用代码部署到客户服务器，用户解密license文件并校验其许可信息。

# 模块打包

> https://mp.weixin.qq.com/s?__biz=MzIwNzYzODIxMw==&mid=2247485375&idx=1&sn=07664bdeae20b775b12a3ce9cd3ea0f2&chksm=970e1c15a0799503a15070ebfd09fda60b065ea51d5b9c1ce32539ad5fdb232a8f92b2053822#rd
