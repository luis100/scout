package eu.scape_project.watch.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.Inject;

import eu.scape_project.watch.web.annotations.Controller;
import eu.scape_project.watch.web.annotations.HttpMethod;
import eu.scape_project.watch.web.annotations.Path;
import eu.scape_project.watch.web.annotations.TemplateSource;

@Path("/upload/policy/new")
@TemplateSource("uploadPolicy")
@HttpMethod({ HttpMethod.Type.GET, HttpMethod.Type.POST })
public class UploadPolicy extends TemplateContext {

  @Inject
  private HttpServletResponse response;

  @Inject
  private HttpServletRequest request;

  private String uploadPath = System.getProperty("user.home") + File.separator + ".scout" + File.separator + "policies";

  public UploadPolicy() {
    File uploadDir = new File(this.uploadPath);
    if (!uploadDir.exists()) {
      uploadDir.mkdirs();
    }
  }

  @Controller(HttpMethod.Type.POST)
  boolean redirectPostData() throws IOException, ServletException {

    if (!ServletFileUpload.isMultipartContent(request)) {
      throw new IllegalArgumentException(
          "Request is not multipart, please 'multipart/form-data' enctype for your form.");
    }

    ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
    PrintWriter writer = response.getWriter();
    response.setContentType("application/json");
    JSONArray response = new JSONArray();
    try {
      List<FileItem> items = uploadHandler.parseRequest(this.request);
      for (FileItem item : items) {
        if (!item.isFormField()) {
          File file = new File(this.uploadPath, item.getName());
          item.write(file);
          JSONObject metadata = new JSONObject();
          metadata.put("name", item.getName());
          metadata.put("size", item.getSize());
          metadata.put("url", this.getContextPath() + this.getMustacheletPath() + "/policies?getfile=" + item.getName());
          // metadata.put("thumbnail_url", "upload?getthumb=" + item.getName());
          metadata.put("delete_url", this.getContextPath() + this.getMustacheletPath() + "/policies?delfile=" + item.getName());
          metadata.put("delete_type", "DELETE");
          response.put(metadata);
        }
      }
    } catch (FileUploadException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      writer.write(response.toString());
      writer.close();
    }

    return false;
  }

  public String getRedirect() {
    return "/web";
  }

  private String getFileName(final Part part) {
    final String partHeader = part.getHeader("content-disposition");
    for (String content : part.getHeader("content-disposition").split(";")) {
      if (content.trim().startsWith("filename")) {
        return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
      }
    }
    return null;
  }

}
