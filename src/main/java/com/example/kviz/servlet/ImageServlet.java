package com.example.kviz.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "/home/haris/Desktop/uploads"; // absolute path
    String uploads = Paths.get(System.getProperty("user.dir"), "uploads").toString();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, IOException {
        String requestedFile = req.getPathInfo(); // e.g. "/quiz/123.jpg"
        System.out.println("Requested file: " + requestedFile);
        File file = new File(uploads, requestedFile);
        System.out.println("Uploading file: " + file.getAbsolutePath());

        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Detect content type
        String mime = getServletContext().getMimeType(file.getName());
        if (mime == null) {
            mime = "application/octet-stream";
        }
        System.out.println("MIME type: " + mime);
        resp.setContentType(mime);

        // Stream the file
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
