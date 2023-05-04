package ru.ivvasch.niolibrary.controller;

import io.minio.GetObjectResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ivvasch.niolibrary.service.MinioService;


@RestController
@RequestMapping("/book")
public class ReadWriteRestController {
    private final MinioService minioService;

    public ReadWriteRestController(MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("{fileName}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws Exception {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        GetObjectResponse objectResponse = minioService.getBookFromMinio(fileName, null);
        IOUtils.copy(objectResponse, response.getOutputStream());
        response.flushBuffer();
        objectResponse.close();
    }
}
