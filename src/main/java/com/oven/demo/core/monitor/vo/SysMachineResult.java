package com.oven.demo.core.monitor.vo;

import java.io.Serializable;

/**
 * 服务监控实体类
 *
 * @author Oven
 */
public class SysMachineResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private SysMachineResult.SysOsInfo sysOsInfo;
    private SysMachineResult.SysJavaInfo sysJavaInfo;
    private SysMachineResult.SysJvmMemInfo sysJvmMemInfo;

    public SysMachineResult() {
    }

    public SysMachineResult.SysOsInfo getSysOsInfo() {
        return this.sysOsInfo;
    }

    public SysMachineResult.SysJavaInfo getSysJavaInfo() {
        return this.sysJavaInfo;
    }

    public SysMachineResult.SysJvmMemInfo getSysJvmMemInfo() {
        return this.sysJvmMemInfo;
    }

    public void setSysOsInfo(SysMachineResult.SysOsInfo sysOsInfo) {
        this.sysOsInfo = sysOsInfo;
    }

    public void setSysJavaInfo(SysMachineResult.SysJavaInfo sysJavaInfo) {
        this.sysJavaInfo = sysJavaInfo;
    }

    public void setSysJvmMemInfo(SysMachineResult.SysJvmMemInfo sysJvmMemInfo) {
        this.sysJvmMemInfo = sysJvmMemInfo;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SysMachineResult)) {
            return false;
        } else {
            SysMachineResult other = (SysMachineResult) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47:
                {
                    Object this$sysOsInfo = this.getSysOsInfo();
                    Object other$sysOsInfo = other.getSysOsInfo();
                    if (this$sysOsInfo == null) {
                        if (other$sysOsInfo == null) {
                            break label47;
                        }
                    } else if (this$sysOsInfo.equals(other$sysOsInfo)) {
                        break label47;
                    }

                    return false;
                }

                Object this$sysJavaInfo = this.getSysJavaInfo();
                Object other$sysJavaInfo = other.getSysJavaInfo();
                if (this$sysJavaInfo == null) {
                    if (other$sysJavaInfo != null) {
                        return false;
                    }
                } else if (!this$sysJavaInfo.equals(other$sysJavaInfo)) {
                    return false;
                }

                Object this$sysJvmMemInfo = this.getSysJvmMemInfo();
                Object other$sysJvmMemInfo = other.getSysJvmMemInfo();
                if (this$sysJvmMemInfo == null) {
                    return other$sysJvmMemInfo == null;
                } else {
                    return this$sysJvmMemInfo.equals(other$sysJvmMemInfo);
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SysMachineResult;
    }

    public int hashCode() {
        int result = 1;
        Object $sysOsInfo = this.getSysOsInfo();
        result = result * 59 + ($sysOsInfo == null ? 43 : $sysOsInfo.hashCode());
        Object $sysJavaInfo = this.getSysJavaInfo();
        result = result * 59 + ($sysJavaInfo == null ? 43 : $sysJavaInfo.hashCode());
        Object $sysJvmMemInfo = this.getSysJvmMemInfo();
        result = result * 59 + ($sysJvmMemInfo == null ? 43 : $sysJvmMemInfo.hashCode());
        return result;
    }

    public String toString() {
        return "SysMachineResult(sysOsInfo=" + this.getSysOsInfo() + ", sysJavaInfo=" + this.getSysJavaInfo() + ", sysJvmMemInfo=" + this.getSysJvmMemInfo() + ")";
    }

    public static class SysJvmMemInfo {
        private String jvmMaxMemory;
        private String jvmUsableMemory;
        private String jvmTotalMemory;
        private String jvmUsedMemory;
        private String jvmFreeMemory;
        private String jvmMemoryUsedRate;

        public SysJvmMemInfo() {
        }

        public String getJvmMaxMemory() {
            return this.jvmMaxMemory;
        }

        public String getJvmUsableMemory() {
            return this.jvmUsableMemory;
        }

        public String getJvmTotalMemory() {
            return this.jvmTotalMemory;
        }

        public String getJvmUsedMemory() {
            return this.jvmUsedMemory;
        }

        public String getJvmFreeMemory() {
            return this.jvmFreeMemory;
        }

        public String getJvmMemoryUsedRate() {
            return this.jvmMemoryUsedRate;
        }

        public void setJvmMaxMemory(String jvmMaxMemory) {
            this.jvmMaxMemory = jvmMaxMemory;
        }

        public void setJvmUsableMemory(String jvmUsableMemory) {
            this.jvmUsableMemory = jvmUsableMemory;
        }

        public void setJvmTotalMemory(String jvmTotalMemory) {
            this.jvmTotalMemory = jvmTotalMemory;
        }

        public void setJvmUsedMemory(String jvmUsedMemory) {
            this.jvmUsedMemory = jvmUsedMemory;
        }

        public void setJvmFreeMemory(String jvmFreeMemory) {
            this.jvmFreeMemory = jvmFreeMemory;
        }

        public void setJvmMemoryUsedRate(String jvmMemoryUsedRate) {
            this.jvmMemoryUsedRate = jvmMemoryUsedRate;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof SysMachineResult.SysJvmMemInfo)) {
                return false;
            } else {
                SysMachineResult.SysJvmMemInfo other = (SysMachineResult.SysJvmMemInfo) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$jvmMaxMemory = this.getJvmMaxMemory();
                    Object other$jvmMaxMemory = other.getJvmMaxMemory();
                    if (this$jvmMaxMemory == null) {
                        if (other$jvmMaxMemory != null) {
                            return false;
                        }
                    } else if (!this$jvmMaxMemory.equals(other$jvmMaxMemory)) {
                        return false;
                    }

                    Object this$jvmUsableMemory = this.getJvmUsableMemory();
                    Object other$jvmUsableMemory = other.getJvmUsableMemory();
                    if (this$jvmUsableMemory == null) {
                        if (other$jvmUsableMemory != null) {
                            return false;
                        }
                    } else if (!this$jvmUsableMemory.equals(other$jvmUsableMemory)) {
                        return false;
                    }

                    Object this$jvmTotalMemory = this.getJvmTotalMemory();
                    Object other$jvmTotalMemory = other.getJvmTotalMemory();
                    if (this$jvmTotalMemory == null) {
                        if (other$jvmTotalMemory != null) {
                            return false;
                        }
                    } else if (!this$jvmTotalMemory.equals(other$jvmTotalMemory)) {
                        return false;
                    }

                    label62:
                    {
                        Object this$jvmUsedMemory = this.getJvmUsedMemory();
                        Object other$jvmUsedMemory = other.getJvmUsedMemory();
                        if (this$jvmUsedMemory == null) {
                            if (other$jvmUsedMemory == null) {
                                break label62;
                            }
                        } else if (this$jvmUsedMemory.equals(other$jvmUsedMemory)) {
                            break label62;
                        }

                        return false;
                    }

                    label55:
                    {
                        Object this$jvmFreeMemory = this.getJvmFreeMemory();
                        Object other$jvmFreeMemory = other.getJvmFreeMemory();
                        if (this$jvmFreeMemory == null) {
                            if (other$jvmFreeMemory == null) {
                                break label55;
                            }
                        } else if (this$jvmFreeMemory.equals(other$jvmFreeMemory)) {
                            break label55;
                        }
                        return false;
                    }

                    Object this$jvmMemoryUsedRate = this.getJvmMemoryUsedRate();
                    Object other$jvmMemoryUsedRate = other.getJvmMemoryUsedRate();
                    if (this$jvmMemoryUsedRate == null) {
                        return other$jvmMemoryUsedRate == null;
                    } else {
                        return this$jvmMemoryUsedRate.equals(other$jvmMemoryUsedRate);
                    }
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof SysMachineResult.SysJvmMemInfo;
        }

        public int hashCode() {
            int result = 1;
            Object $jvmMaxMemory = this.getJvmMaxMemory();
            result = result * 59 + ($jvmMaxMemory == null ? 43 : $jvmMaxMemory.hashCode());
            Object $jvmUsableMemory = this.getJvmUsableMemory();
            result = result * 59 + ($jvmUsableMemory == null ? 43 : $jvmUsableMemory.hashCode());
            Object $jvmTotalMemory = this.getJvmTotalMemory();
            result = result * 59 + ($jvmTotalMemory == null ? 43 : $jvmTotalMemory.hashCode());
            Object $jvmUsedMemory = this.getJvmUsedMemory();
            result = result * 59 + ($jvmUsedMemory == null ? 43 : $jvmUsedMemory.hashCode());
            Object $jvmFreeMemory = this.getJvmFreeMemory();
            result = result * 59 + ($jvmFreeMemory == null ? 43 : $jvmFreeMemory.hashCode());
            Object $jvmMemoryUsedRate = this.getJvmMemoryUsedRate();
            result = result * 59 + ($jvmMemoryUsedRate == null ? 43 : $jvmMemoryUsedRate.hashCode());
            return result;
        }

        public String toString() {
            return "SysMachineResult.SysJvmMemInfo(jvmMaxMemory=" + this.getJvmMaxMemory() + ", jvmUsableMemory=" + this.getJvmUsableMemory() + ", jvmTotalMemory=" + this.getJvmTotalMemory() + ", jvmUsedMemory=" + this.getJvmUsedMemory() + ", jvmFreeMemory=" + this.getJvmFreeMemory() + ", jvmMemoryUsedRate=" + this.getJvmMemoryUsedRate() + ")";
        }
    }

    public static class SysJavaInfo {
        private String jvmName;
        private String jvmVersion;
        private String jvmVendor;
        private String javaName;
        private String javaVersion;

        public SysJavaInfo() {
        }

        public String getJvmName() {
            return this.jvmName;
        }

        public String getJvmVersion() {
            return this.jvmVersion;
        }

        public String getJvmVendor() {
            return this.jvmVendor;
        }

        public String getJavaName() {
            return this.javaName;
        }

        public String getJavaVersion() {
            return this.javaVersion;
        }

        public void setJvmName(String jvmName) {
            this.jvmName = jvmName;
        }

        public void setJvmVersion(String jvmVersion) {
            this.jvmVersion = jvmVersion;
        }

        public void setJvmVendor(String jvmVendor) {
            this.jvmVendor = jvmVendor;
        }

        public void setJavaName(String javaName) {
            this.javaName = javaName;
        }

        public void setJavaVersion(String javaVersion) {
            this.javaVersion = javaVersion;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof SysMachineResult.SysJavaInfo)) {
                return false;
            } else {
                SysMachineResult.SysJavaInfo other = (SysMachineResult.SysJavaInfo) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    label71:
                    {
                        Object this$jvmName = this.getJvmName();
                        Object other$jvmName = other.getJvmName();
                        if (this$jvmName == null) {
                            if (other$jvmName == null) {
                                break label71;
                            }
                        } else if (this$jvmName.equals(other$jvmName)) {
                            break label71;
                        }
                        return false;
                    }

                    Object this$jvmVersion = this.getJvmVersion();
                    Object other$jvmVersion = other.getJvmVersion();
                    if (this$jvmVersion == null) {
                        if (other$jvmVersion != null) {
                            return false;
                        }
                    } else if (!this$jvmVersion.equals(other$jvmVersion)) {
                        return false;
                    }

                    label57:
                    {
                        Object this$jvmVendor = this.getJvmVendor();
                        Object other$jvmVendor = other.getJvmVendor();
                        if (this$jvmVendor == null) {
                            if (other$jvmVendor == null) {
                                break label57;
                            }
                        } else if (this$jvmVendor.equals(other$jvmVendor)) {
                            break label57;
                        }

                        return false;
                    }

                    Object this$javaName = this.getJavaName();
                    Object other$javaName = other.getJavaName();
                    if (this$javaName == null) {
                        if (other$javaName != null) {
                            return false;
                        }
                    } else if (!this$javaName.equals(other$javaName)) {
                        return false;
                    }

                    Object this$javaVersion = this.getJavaVersion();
                    Object other$javaVersion = other.getJavaVersion();
                    if (this$javaVersion == null) {
                        return other$javaVersion == null;
                    } else {
                        return this$javaVersion.equals(other$javaVersion);
                    }
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof SysMachineResult.SysJavaInfo;
        }

        public int hashCode() {
            int result = 1;
            Object $jvmName = this.getJvmName();
            result = result * 59 + ($jvmName == null ? 43 : $jvmName.hashCode());
            Object $jvmVersion = this.getJvmVersion();
            result = result * 59 + ($jvmVersion == null ? 43 : $jvmVersion.hashCode());
            Object $jvmVendor = this.getJvmVendor();
            result = result * 59 + ($jvmVendor == null ? 43 : $jvmVendor.hashCode());
            Object $javaName = this.getJavaName();
            result = result * 59 + ($javaName == null ? 43 : $javaName.hashCode());
            Object $javaVersion = this.getJavaVersion();
            result = result * 59 + ($javaVersion == null ? 43 : $javaVersion.hashCode());
            return result;
        }

        public String toString() {
            return "SysMachineResult.SysJavaInfo(jvmName=" + this.getJvmName() + ", jvmVersion=" + this.getJvmVersion() + ", jvmVendor=" + this.getJvmVendor() + ", javaName=" + this.getJavaName() + ", javaVersion=" + this.getJavaVersion() + ")";
        }
    }

    public static class SysOsInfo {
        private String osName;
        private String osArch;
        private String osVersion;
        private String osHostName;
        private String osHostAddress;

        public SysOsInfo() {
        }

        public String getOsName() {
            return this.osName;
        }

        public String getOsArch() {
            return this.osArch;
        }

        public String getOsVersion() {
            return this.osVersion;
        }

        public String getOsHostName() {
            return this.osHostName;
        }

        public String getOsHostAddress() {
            return this.osHostAddress;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public void setOsArch(String osArch) {
            this.osArch = osArch;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public void setOsHostName(String osHostName) {
            this.osHostName = osHostName;
        }

        public void setOsHostAddress(String osHostAddress) {
            this.osHostAddress = osHostAddress;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof SysMachineResult.SysOsInfo)) {
                return false;
            } else {
                SysMachineResult.SysOsInfo other = (SysMachineResult.SysOsInfo) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    label71:
                    {
                        Object this$osName = this.getOsName();
                        Object other$osName = other.getOsName();
                        if (this$osName == null) {
                            if (other$osName == null) {
                                break label71;
                            }
                        } else if (this$osName.equals(other$osName)) {
                            break label71;
                        }
                        return false;
                    }

                    Object this$osArch = this.getOsArch();
                    Object other$osArch = other.getOsArch();
                    if (this$osArch == null) {
                        if (other$osArch != null) {
                            return false;
                        }
                    } else if (!this$osArch.equals(other$osArch)) {
                        return false;
                    }

                    label57:
                    {
                        Object this$osVersion = this.getOsVersion();
                        Object other$osVersion = other.getOsVersion();
                        if (this$osVersion == null) {
                            if (other$osVersion == null) {
                                break label57;
                            }
                        } else if (this$osVersion.equals(other$osVersion)) {
                            break label57;
                        }

                        return false;
                    }

                    Object this$osHostName = this.getOsHostName();
                    Object other$osHostName = other.getOsHostName();
                    if (this$osHostName == null) {
                        if (other$osHostName != null) {
                            return false;
                        }
                    } else if (!this$osHostName.equals(other$osHostName)) {
                        return false;
                    }

                    Object this$osHostAddress = this.getOsHostAddress();
                    Object other$osHostAddress = other.getOsHostAddress();
                    if (this$osHostAddress == null) {
                        return other$osHostAddress == null;
                    } else {
                        return this$osHostAddress.equals(other$osHostAddress);
                    }
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof SysMachineResult.SysOsInfo;
        }

        public int hashCode() {
            int result = 1;
            Object $osName = this.getOsName();
            result = result * 59 + ($osName == null ? 43 : $osName.hashCode());
            Object $osArch = this.getOsArch();
            result = result * 59 + ($osArch == null ? 43 : $osArch.hashCode());
            Object $osVersion = this.getOsVersion();
            result = result * 59 + ($osVersion == null ? 43 : $osVersion.hashCode());
            Object $osHostName = this.getOsHostName();
            result = result * 59 + ($osHostName == null ? 43 : $osHostName.hashCode());
            Object $osHostAddress = this.getOsHostAddress();
            result = result * 59 + ($osHostAddress == null ? 43 : $osHostAddress.hashCode());
            return result;
        }

        public String toString() {
            return "SysMachineResult.SysOsInfo(osName=" + this.getOsName() + ", osArch=" + this.getOsArch() + ", osVersion=" + this.getOsVersion() + ", osHostName=" + this.getOsHostName() + ", osHostAddress=" + this.getOsHostAddress() + ")";
        }
    }

}
