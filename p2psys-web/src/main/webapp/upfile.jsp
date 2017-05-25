<%@page import="java.io.*,sun.misc.*"%>
<%@page import="com.rongdu.common.util.ImageUtil"%>
<%
	String pic1 = request.getParameter("png1");
	String pic2 = request.getParameter("png2");
	String pic3 = request.getParameter("png3");
	String userId = request.getParameter("userId");

	String path = request.getSession().getServletContext().getRealPath("/") + "/data/avatar/";

	//图1(大)
	File file1 = new File(path + userId + "_avatar_big.jpg");
	if (!ImageUtil.fileIsImage(file1)) {
		out.println("error");
	}

	FileOutputStream fout1 = null;
	fout1 = new FileOutputStream(file1);
	fout1.write(new BASE64Decoder().decodeBuffer(pic1));
	fout1.close();

	//图2(中)
	File file2 = new File(path + userId + "_avatar_middle.jpg");
	if (!ImageUtil.fileIsImage(file2)) {
		out.println("error");
	}
	FileOutputStream fout2 = null;
	fout2 = new FileOutputStream(file2);
	fout2.write(new BASE64Decoder().decodeBuffer(pic2));
	fout2.close();

	//图3(小)
	File file3 = new File(path + userId + "_avatar_small.jpg");
	if (!ImageUtil.fileIsImage(file3)) {
		out.println("error");
	}
	FileOutputStream fout3 = null;
	fout3 = new FileOutputStream(file3);
	fout3.write(new BASE64Decoder().decodeBuffer(pic3));
	fout3.close();

	out.println("success=保存成功");
%>