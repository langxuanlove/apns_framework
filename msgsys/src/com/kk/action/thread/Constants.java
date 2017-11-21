/*
 * Copyright (c) 2012-2014 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package com.kk.action.thread;

/**
 * Server constants keeper
 */
public class Constants {
    public static final String ATTR_CLIENTID = "ClientID";
    public static final String CLEAN_SESSION = "cleanSession";
    public static final String KEEP_ALIVE = "keepAlive";
    public static final String SOS = "1"; //手动报警
    public static final String close = "6"; //关机事件
    public static final String notOnline = "0"; //设备不在线
    public static final String batteryLow = "4"; //电池电量低
    public static final String illegalOpenDoor = "9"; //开关输入报警
    public static final String beyondSafetyArea = "3"; //出圈报警
    public static final String inSafetyArea = "8"; //入圈报警消息
    public static final String open = "5"; //开机事件
    public static final String VIB = "10"; //震动报警
    public static final String TESTREPORT = "18"; //测试报告
    public static final String REALLOCATION = "16"; //实时位置
    public static final String OPEN_SUCCESS = "12"; //追踪模式开启中
    public static final String CLOSE_SUCCESS = "14"; //追踪模式已关闭
    public static final String ChangeFrequency_SUCCESS = "19"; //设备定位频率成功
    public static final String DeviceSetting_SUCCESS = "15"; //设备设置成功
}
