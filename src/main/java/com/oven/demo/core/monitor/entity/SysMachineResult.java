package com.oven.demo.core.monitor.entity;

import java.io.Serializable;

/**
 * 服务监控实体类
 *
 * @author Oven
 */
public class SysMachineResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private SysOsInfo sysOsInfo;
    private SysJavaInfo sysJavaInfo;
    private SysJvmMemInfo sysJvmMemInfo;

    public SysMachineResult() {
    }

    public SysOsInfo getSysOsInfo() {
        return this.sysOsInfo;
    }

    public SysJavaInfo getSysJavaInfo() {
        return this.sysJavaInfo;
    }

    public SysJvmMemInfo getSysJvmMemInfo() {
        return this.sysJvmMemInfo;
    }

    public void setSysOsInfo(SysOsInfo sysOsInfo) {
        this.sysOsInfo = sysOsInfo;
    }

    public void setSysJavaInfo(SysJavaInfo sysJavaInfo) {
        this.sysJavaInfo = sysJavaInfo;
    }

    public void setSysJvmMemInfo(SysJvmMemInfo sysJvmMemInfo) {
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
                    Object thisSysOsInfo = this.getSysOsInfo();
                    Object otherSysOsInfo = other.getSysOsInfo();
                    if (thisSysOsInfo == null) {
                        if (otherSysOsInfo == null) {
                            break label47;
                        }
                    } else if (thisSysOsInfo.equals(otherSysOsInfo)) {
                        break label47;
                    }

                    return false;
                }

                Object thisSysJavaInfo = this.getSysJavaInfo();
                Object otherSysJavaInfo = other.getSysJavaInfo();
                if (thisSysJavaInfo == null) {
                    if (otherSysJavaInfo != null) {
                        return false;
                    }
                } else if (!thisSysJavaInfo.equals(otherSysJavaInfo)) {
                    return false;
                }

                Object thisSysJvmMemInfo = this.getSysJvmMemInfo();
                Object otherSysJvmMemInfo = other.getSysJvmMemInfo();
                if (thisSysJvmMemInfo == null) {
                    return otherSysJvmMemInfo == null;
                } else {
                    return thisSysJvmMemInfo.equals(otherSysJvmMemInfo);
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SysMachineResult;
    }

    public int hashCode() {
        int result = 1;
        Object sysOsInfo = this.getSysOsInfo();
        result = result * 59 + (sysOsInfo == null ? 43 : sysOsInfo.hashCode());
        Object sysJavaInfo = this.getSysJavaInfo();
        result = result * 59 + (sysJavaInfo == null ? 43 : sysJavaInfo.hashCode());
        Object sysJvmMemInfo = this.getSysJvmMemInfo();
        result = result * 59 + (sysJvmMemInfo == null ? 43 : sysJvmMemInfo.hashCode());
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
            } else if (!(o instanceof SysJvmMemInfo)) {
                return false;
            } else {
                SysJvmMemInfo other = (SysJvmMemInfo) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object thisJvmMaxMemory = this.getJvmMaxMemory();
                    Object otherJvmMaxMemory = other.getJvmMaxMemory();
                    if (thisJvmMaxMemory == null) {
                        if (otherJvmMaxMemory != null) {
                            return false;
                        }
                    } else if (!thisJvmMaxMemory.equals(otherJvmMaxMemory)) {
                        return false;
                    }

                    Object thisJvmUsableMemory = this.getJvmUsableMemory();
                    Object otherJvmUsableMemory = other.getJvmUsableMemory();
                    if (thisJvmUsableMemory == null) {
                        if (otherJvmUsableMemory != null) {
                            return false;
                        }
                    } else if (!thisJvmUsableMemory.equals(otherJvmUsableMemory)) {
                        return false;
                    }

                    Object thisJvmTotalMemory = this.getJvmTotalMemory();
                    Object otherJvmTotalMemory = other.getJvmTotalMemory();
                    if (thisJvmTotalMemory == null) {
                        if (otherJvmTotalMemory != null) {
                            return false;
                        }
                    } else if (!thisJvmTotalMemory.equals(otherJvmTotalMemory)) {
                        return false;
                    }

                    label62:
                    {
                        Object thisJvmUsedMemory = this.getJvmUsedMemory();
                        Object otherJvmUsedMemory = other.getJvmUsedMemory();
                        if (thisJvmUsedMemory == null) {
                            if (otherJvmUsedMemory == null) {
                                break label62;
                            }
                        } else if (thisJvmUsedMemory.equals(otherJvmUsedMemory)) {
                            break label62;
                        }

                        return false;
                    }

                    label55:
                    {
                        Object thisJvmFreeMemory = this.getJvmFreeMemory();
                        Object otherJvmFreeMemory = other.getJvmFreeMemory();
                        if (thisJvmFreeMemory == null) {
                            if (otherJvmFreeMemory == null) {
                                break label55;
                            }
                        } else if (thisJvmFreeMemory.equals(otherJvmFreeMemory)) {
                            break label55;
                        }
                        return false;
                    }

                    Object thisJvmMemoryUsedRate = this.getJvmMemoryUsedRate();
                    Object otherJvmMemoryUsedRate = other.getJvmMemoryUsedRate();
                    if (thisJvmMemoryUsedRate == null) {
                        return otherJvmMemoryUsedRate == null;
                    } else {
                        return thisJvmMemoryUsedRate.equals(otherJvmMemoryUsedRate);
                    }
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof SysJvmMemInfo;
        }

        public int hashCode() {
            int result = 1;
            Object jvmMaxMemory = this.getJvmMaxMemory();
            result = result * 59 + (jvmMaxMemory == null ? 43 : jvmMaxMemory.hashCode());
            Object jvmUsableMemory = this.getJvmUsableMemory();
            result = result * 59 + (jvmUsableMemory == null ? 43 : jvmUsableMemory.hashCode());
            Object jvmTotalMemory = this.getJvmTotalMemory();
            result = result * 59 + (jvmTotalMemory == null ? 43 : jvmTotalMemory.hashCode());
            Object jvmUsedMemory = this.getJvmUsedMemory();
            result = result * 59 + (jvmUsedMemory == null ? 43 : jvmUsedMemory.hashCode());
            Object jvmFreeMemory = this.getJvmFreeMemory();
            result = result * 59 + (jvmFreeMemory == null ? 43 : jvmFreeMemory.hashCode());
            Object jvmMemoryUsedRate = this.getJvmMemoryUsedRate();
            result = result * 59 + (jvmMemoryUsedRate == null ? 43 : jvmMemoryUsedRate.hashCode());
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
            } else if (!(o instanceof SysJavaInfo)) {
                return false;
            } else {
                SysJavaInfo other = (SysJavaInfo) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    label71:
                    {
                        Object thisJvmName = this.getJvmName();
                        Object otherJvmName = other.getJvmName();
                        if (thisJvmName == null) {
                            if (otherJvmName == null) {
                                break label71;
                            }
                        } else if (thisJvmName.equals(otherJvmName)) {
                            break label71;
                        }
                        return false;
                    }

                    Object thisJvmVersion = this.getJvmVersion();
                    Object otherJvmVersion = other.getJvmVersion();
                    if (thisJvmVersion == null) {
                        if (otherJvmVersion != null) {
                            return false;
                        }
                    } else if (!thisJvmVersion.equals(otherJvmVersion)) {
                        return false;
                    }

                    label57:
                    {
                        Object thisJvmVendor = this.getJvmVendor();
                        Object otherJvmVendor = other.getJvmVendor();
                        if (thisJvmVendor == null) {
                            if (otherJvmVendor == null) {
                                break label57;
                            }
                        } else if (thisJvmVendor.equals(otherJvmVendor)) {
                            break label57;
                        }

                        return false;
                    }

                    Object thisJavaName = this.getJavaName();
                    Object otherJavaName = other.getJavaName();
                    if (thisJavaName == null) {
                        if (otherJavaName != null) {
                            return false;
                        }
                    } else if (!thisJavaName.equals(otherJavaName)) {
                        return false;
                    }

                    Object thisJavaVersion = this.getJavaVersion();
                    Object otherJavaVersion = other.getJavaVersion();
                    if (thisJavaVersion == null) {
                        return otherJavaVersion == null;
                    } else {
                        return thisJavaVersion.equals(otherJavaVersion);
                    }
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof SysJavaInfo;
        }

        public int hashCode() {
            int result = 1;
            Object jvmName = this.getJvmName();
            result = result * 59 + (jvmName == null ? 43 : jvmName.hashCode());
            Object jvmVersion = this.getJvmVersion();
            result = result * 59 + (jvmVersion == null ? 43 : jvmVersion.hashCode());
            Object jvmVendor = this.getJvmVendor();
            result = result * 59 + (jvmVendor == null ? 43 : jvmVendor.hashCode());
            Object javaName = this.getJavaName();
            result = result * 59 + (javaName == null ? 43 : javaName.hashCode());
            Object javaVersion = this.getJavaVersion();
            result = result * 59 + (javaVersion == null ? 43 : javaVersion.hashCode());
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
            } else if (!(o instanceof SysOsInfo)) {
                return false;
            } else {
                SysOsInfo other = (SysOsInfo) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    label71:
                    {
                        Object thisOsName = this.getOsName();
                        Object otherOsName = other.getOsName();
                        if (thisOsName == null) {
                            if (otherOsName == null) {
                                break label71;
                            }
                        } else if (thisOsName.equals(otherOsName)) {
                            break label71;
                        }
                        return false;
                    }

                    Object thisOsArch = this.getOsArch();
                    Object otherOsArch = other.getOsArch();
                    if (thisOsArch == null) {
                        if (otherOsArch != null) {
                            return false;
                        }
                    } else if (!thisOsArch.equals(otherOsArch)) {
                        return false;
                    }

                    label57:
                    {
                        Object thisOsVersion = this.getOsVersion();
                        Object otherOsVersion = other.getOsVersion();
                        if (thisOsVersion == null) {
                            if (otherOsVersion == null) {
                                break label57;
                            }
                        } else if (thisOsVersion.equals(otherOsVersion)) {
                            break label57;
                        }

                        return false;
                    }

                    Object thisOsHostName = this.getOsHostName();
                    Object otherOsHostName = other.getOsHostName();
                    if (thisOsHostName == null) {
                        if (otherOsHostName != null) {
                            return false;
                        }
                    } else if (!thisOsHostName.equals(otherOsHostName)) {
                        return false;
                    }

                    Object thisOsHostAddress = this.getOsHostAddress();
                    Object otherOsHostAddress = other.getOsHostAddress();
                    if (thisOsHostAddress == null) {
                        return otherOsHostAddress == null;
                    } else {
                        return thisOsHostAddress.equals(otherOsHostAddress);
                    }
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof SysOsInfo;
        }

        public int hashCode() {
            int result = 1;
            Object osName = this.getOsName();
            result = result * 59 + (osName == null ? 43 : osName.hashCode());
            Object osArch = this.getOsArch();
            result = result * 59 + (osArch == null ? 43 : osArch.hashCode());
            Object osVersion = this.getOsVersion();
            result = result * 59 + (osVersion == null ? 43 : osVersion.hashCode());
            Object osHostName = this.getOsHostName();
            result = result * 59 + (osHostName == null ? 43 : osHostName.hashCode());
            Object osHostAddress = this.getOsHostAddress();
            result = result * 59 + (osHostAddress == null ? 43 : osHostAddress.hashCode());
            return result;
        }

        public String toString() {
            return "SysMachineResult.SysOsInfo(osName=" + this.getOsName() + ", osArch=" + this.getOsArch() + ", osVersion=" + this.getOsVersion() + ", osHostName=" + this.getOsHostName() + ", osHostAddress=" + this.getOsHostAddress() + ")";
        }
    }

}
