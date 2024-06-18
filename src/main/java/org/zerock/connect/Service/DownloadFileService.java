package org.zerock.connect.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class DownloadFileService {

    
//    윈도우 경로
    private static final String UPLOAD_DIR = "C:\\upload\\";
    
    
//    우분투 경로
//    private static final String UPLOAD_DIR = "/home/mit305/back/connect/images/";


    public void download(String fileName, HttpServletResponse response) {
        File file = new File(UPLOAD_DIR + fileName);

        if (file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentLength((int) file.length());

            try (FileInputStream fis = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }

                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            System.out.println("[DownloadFileService] FILE NOT FOUND: " + fileName);
        }
    }
}
