package com.testck.action;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CkeditorUpload extends ActionSupport {
	private File upload;
	private String uploadContentType;
	private String uploadFileName;

	private int merchantId;

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {

		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String execute() throws Exception {
		System.out.println("dddddddddddddddd");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		// CKEditor提交的很重要的一个参数
		String callback = ServletActionContext.getRequest().getParameter(
				"CKEditorFuncNum");

		String expandedName = ""; // 文件扩展名
		if (uploadContentType.equals("image/pjpeg")
				|| uploadContentType.equals("image/jpeg")) {
			// IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg
			expandedName = ".jpg";
		} else if (uploadContentType.equals("image/png")
				|| uploadContentType.equals("image/x-png")) {
			// IE6上传的png图片的headimageContentType是"image/x-png"
			expandedName = ".png";
		} else if (uploadContentType.equals("image/gif")) {
			expandedName = ".gif";
		} else if (uploadContentType.equals("image/bmp")) {
			expandedName = ".bmp";
		} else {
			out.println("<script type=\"text/javascript\">");
			out.println("window.parent.CKEDITOR.tools.callFunction(" + callback
					+ ",''," + "'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");
			out.println("</script>");
			return null;
		}

		if (upload.length() > 600 * 1024) {
			out.println("<script type=\"text/javascript\">");
			out.println("window.parent.CKEDITOR.tools.callFunction(" + callback
					+ ",''," + "'文件大小不得大于600k');");
			out.println("</script>");
			return null;
		}

		InputStream is = new FileInputStream(upload);
		String uploadPath = ServletActionContext.getServletContext()
				.getRealPath("/img/postImg");
		if (merchantId > 0) {
			uploadPath += "/" + merchantId;
		}
		// 创建文件目录
		File savedir = new File(uploadPath);

		// 如果目录不存在就创建
		if (!savedir.exists()) {
			savedir.mkdirs();
		}

		Image img = javax.imageio.ImageIO.read(upload);
		int wideth = img.getWidth(null); // 得到源图宽

		int height = img.getHeight(null); // 得到源图长
		int nw = wideth;
		int nh = height;
		if (wideth > 720) {
			nw = 720;

			nh = (int) ((double) height / ((double) wideth / (double) nw));

		}

		String fileName = java.util.UUID.randomUUID().toString(); // 采用时间+UUID的方式随即命名
		fileName += expandedName;
		BufferedImage tag = new BufferedImage(nw, nh,

		BufferedImage.TYPE_INT_RGB);

		tag.getGraphics()

		.drawImage(img, 0, 0, nw, nh, null); // 绘制后的图

		FileOutputStream ou = new FileOutputStream(uploadPath + "/" + fileName);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(ou);

		encoder.encode(tag); // 近JPEG编码

		ou.close();

		/*
		 * 
		 * 
		 * File toFile = new File(uploadPath, fileName); OutputStream os = new
		 * FileOutputStream(toFile); byte[] buffer = new byte[1024]; int length
		 * = 0; while ((length = is.read(buffer)) > 0) { os.write(buffer, 0,
		 * length); } is.close(); os.close();
		 */
		// 返回“图像”选项卡并显示图片
		String spath = "img/postImg/";
		if (merchantId > 0) {
			spath += merchantId + "/";
		}
		out.println("<script type=\"text/javascript\">");
		out.println("window.parent.CKEDITOR.tools.callFunction(" + callback
				+ ",'" + spath + fileName + "','')");
		out.println("</script>");

		return null;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

}
