package com.Client;

import java.io.DataOutputStream;
import java.io.IOException;

public class ToServer {
	// 给服务器端发送消息
	public static void sendMsg(String str, DataOutputStream dos) throws IOException {
		System.out.println("发送给服务器：" + str);
		dos.write(str.getBytes());
	}
}
