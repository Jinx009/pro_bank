
package com.rongdu.p2psys.core.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;



public class SocketClient {
	public static String sendSocketMsg(String ip,int port,String sendMsg) {
		
		Socket socket = null;
		String rtnMsg = null;
		BufferedReader in =null;
		try {
			// 创建一个流套接字并将其连接到指定主机上的指定端口号
			socket = new Socket(ip, port);
			socket.setSoTimeout(100000);
			// 读取服务器端数据
			DataInputStream input = new DataInputStream(socket.getInputStream());
			// 向服务器端发送数据
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			//进行数据加密
			byte[] desCrypto =sendMsg.getBytes();// DESUtils.desCrypto(sendMsg.getBytes("UTF-8"));
			out.write(desCrypto);
			out.flush();

			int count = 0;
			while(count == 0){
				count = input.available();
			}
			byte[] bytes = new byte[count];
			input.read(bytes);
			//进行数据解密
		//	byte[] rebytes = DESUtils.decrypt(bytes);
			rtnMsg = new String(bytes,"utf-8");
			out.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
			rtnMsg = "客户端异常:" + e.getMessage();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
					rtnMsg = "客户端 finally 异常:" + e.getMessage();
				}
			}
		}
		
		return rtnMsg;
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
	Map<String, String> map = new HashMap<String, String>();
	map.put("ordDate", "20141216");
	map.put("ordNo", "123123");
	map.put("inTransferCode", "40000");
	String json = JSON.toJSONString(map);
		String sendSocketMsg = SocketClient.sendSocketMsg("10.0.0.128", 10088, json);
		System.out.println("服务端响应报文为:"+sendSocketMsg);
	}
}
