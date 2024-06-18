package org.zerock.connect.Controller;



import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zerock.connect.Service.DownloadFileService;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private DownloadFileService downloadFileService;

//    @GetMapping("/download/{fileName}")
//    public String downloadFile(@PathVariable String fileName, HttpServletResponse response) {
//        downloadFileService.download(fileName, response);
//        return "redirect:/part1/itemForm";
//    }
}
