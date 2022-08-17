package com.zuiyu.common.bean;

import java.util.List;

/**
 * 自定义需要校验的License参数
 */
public class LicenseCheckModel {

//    private static final long serialVersionUID = 8600137500316662317L;
    /**
     * 可被允许的IP地址
     */
    private Boolean checkIp;
    private List<String> ipAddress;
    private Boolean checkMac;
    /**
     * 可被允许的MAC地址
     */
    private List<String> macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;
    private Boolean checkCpu;
    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;
    private Boolean checkMainBoard;

    public List<String> getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(List<String> ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<String> getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(List<String> macAddress) {
        this.macAddress = macAddress;
    }

    public String getCpuSerial() {
        return cpuSerial;
    }

    public void setCpuSerial(String cpuSerial) {
        this.cpuSerial = cpuSerial;
    }

    public String getMainBoardSerial() {
        return mainBoardSerial;
    }

    public void setMainBoardSerial(String mainBoardSerial) {
        this.mainBoardSerial = mainBoardSerial;
    }

    public Boolean getCheckIp() {
        return checkIp;
    }

    public void setCheckIp(Boolean checkIp) {
        this.checkIp = checkIp;
    }

    public Boolean getCheckMac() {
        return checkMac;
    }

    public void setCheckMac(Boolean checkMac) {
        this.checkMac = checkMac;
    }

    public Boolean getCheckCpu() {
        return checkCpu;
    }

    public void setCheckCpu(Boolean checkCpu) {
        this.checkCpu = checkCpu;
    }

    public Boolean getCheckMainBoard() {
        return checkMainBoard;
    }

    public void setCheckMainBoard(Boolean checkMainBoard) {
        this.checkMainBoard = checkMainBoard;
    }

    @Override
    public String toString() {
        return "LicenseCheckModel{" +
                "ipAddress=" + ipAddress +
                ", macAddress=" + macAddress +
                ", cpuSerial='" + cpuSerial + '\'' +
                ", mainBoardSerial='" + mainBoardSerial + '\'' +
                '}';
    }
}
