package Service;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFrame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.container.ParamQualifier;

@Path("/image")
public class ImageService {
	@POST
	@Path("/compression")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("image/jpg")
	public Response compression(
			@FormDataParam("quality") float quality,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		
		String uploadedFileLocation = "E:\\JavaWorkspace\\Images\\" + fileDetail.getFileName();
		writeToFile(uploadedInputStream, uploadedFileLocation);
		File file = new File("E:\\JavaWorkspace\\Images\\" + fileDetail.getFileName());
		/***** sýkýstýrma *////
		BufferedImage image = ImageIO.read(file);
		compressAndShow(image, quality);
			
		File file2 = new File("E:\\JavaWorkspace\\Images\\" + "compress." + quality + ".jpg");
		ResponseBuilder response = Response.ok((Object) file2);
		response.header("Content-Disposition", "attachment; filename=image_from_server.jpg");
		return response.build();
	}

	@POST
	@Path("/segmentation")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response segmentation(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) throws ClientProtocolException, IOException {
		
		System.out.println(fileDetail.getFileName());
		String uploadedFileLocation = "E:\\JavaWorkspace\\Images\\" + fileDetail.getFileName();
		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);
		// String output = "File uploaded to : " + uploadedFileLocation;

		File inFile = new File("E:\\JavaWorkspace\\Images\\" + fileDetail.getFileName());
		FileInputStream fis = null;

		fis = new FileInputStream(inFile);
		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

		// server back-end URL
		HttpPost httppost = new HttpPost("https://still-crag-64816.herokuapp.com/");
		MultipartEntity entity = new MultipartEntity();
		// set the file input stream and file name as arguments
		entity.addPart("file", new InputStreamBody(fis, inFile.getName()));
		httppost.setEntity(entity);
		// execute the request
		HttpResponse response = httpclient.execute(httppost);

		int statusCode = response.getStatusLine().getStatusCode();
		HttpEntity responseEntity = response.getEntity();
		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		System.out.println(responseString);
		
		int beginIndex = responseString.indexOf("<ul>");
		int endIndex  = responseString.indexOf("</ul>");
		String animal = responseString.substring(beginIndex, endIndex);
		return Response.status(200).entity(animal).build();
		// System.out.println("[" + statusCode + "] " + responseString);

	}

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void compressAndShow(BufferedImage image, float quality) throws IOException {
		// Jpeg formatý için bir imagewriter
		Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpg");
		if (!writers.hasNext())
			throw new IllegalStateException("No writers found");
		ImageWriter writer = (ImageWriter) writers.next();
		// Sýkýþtýrýlan resim için bir ImageParam
		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(32768);
		ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
		writer.setOutput(ios);
		writer.write(null, new IIOImage(image, null, null), param);
		ios.flush();
			
		// SIkýþtýrdýðýmýz yeni resim dosyamýzý uygulamamýzýn çalýþtýðý dizine
		// kaydediyor
		File file = new File("E:\\JavaWorkspace\\Images\\" + "compress." + quality + ".jpg");
		FileImageOutputStream output = new FileImageOutputStream(file);
		writer.setOutput(output);
		writer.write(null, new IIOImage(image, null, null), param);
	}
}