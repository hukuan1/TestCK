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

		// CKEditor�ύ�ĺ���Ҫ��һ������
		String callback = ServletActionContext.getRequest().getParameter(
				"CKEditorFuncNum");

		String expandedName = ""; // �ļ���չ��
		if (uploadContentType.equals("image/pjpeg")
				|| uploadContentType.equals("image/jpeg")) {
			// IE6�ϴ�jpgͼƬ��headimageContentType��image/pjpeg����IE9�Լ�����ϴ���jpgͼƬ��image/jpeg
			expandedName = ".jpg";
		} else if (uploadContentType.equals("image/png")
				|| uploadContentType.equals("image/x-png")) {
			// IE6�ϴ���pngͼƬ��headimageContentType��"image/x-png"
			expandedName = ".png";
		} else if (uploadContentType.equals("image/gif")) {
			expandedName = ".gif";
		} else if (uploadContentType.equals("image/bmp")) {
			expandedName = ".bmp";
		} else {
			out.println("<script type=\"text/javascript\">");
			out.println("window.parent.CKEDITOR.tools.callFunction(" + callback
					+ ",''," + "'�ļ���ʽ����ȷ������Ϊ.jpg/.gif/.bmp/.png�ļ���');");
			out.println("</script>");
			return null;
		}

		if (upload.length() > 600 * 1024) {
			out.println("<script type=\"text/javascript\">");
			out.println("window.parent.CKEDITOR.tools.callFunction(" + callback
					+ ",''," + "'�ļ���С���ô���600k');");
			out.println("</script>");
			return null;
		}

		InputStream is = new FileInputStream(upload);
		String uploadPath = ServletActionContext.getServletContext()
				.getRealPath("/img/postImg");
		if (merchantId > 0) {
			uploadPath += "/" + merchantId;
		}
		// �����ļ�Ŀ¼
		File savedir = new File(uploadPath);

		// ���Ŀ¼�����ھʹ���
		if (!savedir.exists()) {
			savedir.mkdirs();
		}

		Image img = javax.imageio.ImageIO.read(upload);
		int wideth = img.getWidth(null); // �õ�Դͼ��

		int height = img.getHeight(null); // �õ�Դͼ��
		int nw = wideth;
		int nh = height;
		if (wideth > 720) {
			nw = 720;

			nh = (int) ((double) height / ((double) wideth / (double) nw));

		}

		String fileName = java.util.UUID.randomUUID().toString(); // ����ʱ��+UUID�ķ�ʽ�漴����
		fileName += expandedName;
		BufferedImage tag = new BufferedImage(nw, nh,

		BufferedImage.TYPE_INT_RGB);

		tag.getGraphics()

		.drawImage(img, 0, 0, nw, nh, null); // ���ƺ��ͼ

		FileOutputStream ou = new FileOutputStream(uploadPath + "/" + fileName);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(ou);

		encoder.encode(tag); // ��JPEG����

		ou.close();

		/*
		 * 
		 * 
		 * File toFile = new File(uploadPath, fileName); OutputStream os = new
		 * FileOutputStream(toFile); byte[] buffer = new byte[1024]; int length
		 * = 0; while ((length = is.read(buffer)) > 0) { os.write(buffer, 0,
		 * length); } is.close(); os.close();
		 */
		// ���ء�ͼ��ѡ�����ʾͼƬ
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
