# 智能终端取证系统

## 项目环境：

Android 9

## 功能介绍：

 Android 安全取证系统所有功能以静默形式工作，可实现证据内容的
安全上传。
 监控取证设备基本物理信息（包括设备型号、序列号、无线局域网地
址MAC、蓝牙地址、IMEI 序列号、ICCID 等）。
 监控取证通话记录（录音）。
 监控取证短信来往。

客户端所有信息获取后以json格式保存到txt文件中加密上传至远程服务器。

## 模块简介：

要求所有功能静默方式工作，所以所有功能都包含在MyService.java文件中的Service继承类里。

### 1、设备信息的获取：

安卓手机定位信息的获取:
![image](https://github.com/zhangchi991022/Android-evidence-obtaining-system/blob/main/image/1.PNG)
![image](https://github.com/zhangchi991022/Android-evidence-obtaining-system/blob/main/image/2.PNG)


WiFI Mac的获取：
![image](https://github.com/zhangchi991022/Android-evidence-obtaining-system/blob/main/image/3.png)


### 2、通话记录的获取：
![image](https://github.com/zhangchi991022/Android-evidence-obtaining-system/blob/main/image/4.PNG)


### 3、短信记录的获取：

![image](https://github.com/zhangchi991022/Android-evidence-obtaining-system/blob/main/image/5.PNG)

### 4、数据加密上传至服务器（DES加密）：
![image](https://github.com/zhangchi991022/Android-evidence-obtaining-system/blob/main/image/6.png)


### 5、客户端、服务器通信（传文件）：

Client.java和Server.java使用socket进行数据通信
### 6、运行结果：
https://github.com/zhangchi991022/Android-evidence-obtaining-system/blob/main/7.png
https://github.com/zhangchi991022/Android-evidence-obtaining-system/blob/main/8.png

