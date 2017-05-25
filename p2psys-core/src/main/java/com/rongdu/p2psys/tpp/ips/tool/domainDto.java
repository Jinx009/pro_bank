package com.rongdu.p2psys.tpp.ips.tool;

import java.lang.reflect.Method;


public class domainDto {
	static String dtoPath = "com.rongdu.p2psys.tpp.ips.model.IpsRegisterGuarantor";
	public static void main(String[] args) {
		try {
			String[] dtoNames = dtoPath.split("\\.");
			String dtoName = dtoNames[dtoNames.length - 1];			
			String smallDtoName = toHeadLow(dtoName);
			Class<?> dtoClass = Class.forName(dtoPath);
	
			String paramNames = "";
			
			
			/**
			 * 回调xml解析封装成对象
			 * @param xml
			 * @return
			 */
			
			System.out.printf("/**\r\n");
			System.out.printf(" * 回调xml解析封装成对象\r\n");
			System.out.printf(" * @param xml\r\n");
			System.out.printf(" * @return "+smallDtoName+"\r\n");
			System.out.printf("*/\r\n");
			
			System.out.printf("public " + dtoName + " doReturnCreate(String xml) {\r\n");
			System.out.printf("\t"+dtoName+" "+smallDtoName+" = new "+dtoName+"();\r\n");
			System.out.printf("\tXmlTool tool = new XmlTool();\r\n");
			System.out.printf("\ttool.SetDocument(xml);\r\n");
			for (Method method : dtoClass.getMethods()) {
				String name = method.getName();	
				if (name.equals("getClass"))
					continue;
				if (name.startsWith("get")) {
					String property = name.substring(3);
					paramNames = paramNames+"\""+property+"\",";
					System.out.printf("\t" + smallDtoName + ".set" + property + "(tool.getNodeValue(\"p" + property + "\"));\r\n");
				}
			}	
			System.out.printf("\treturn " + smallDtoName + ";\r\n");
			System.out.printf("} \r\n\r\n");
			
			System.out.printf("private String[] paramNames = new String[]{"+paramNames+"};");
			System.out.printf("\n\n");
		}
		catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	
	public static String toHeadLow(String data) {
		return String.valueOf(Character.toLowerCase(data.charAt(0))) + data.substring(1);
	}
	
}
