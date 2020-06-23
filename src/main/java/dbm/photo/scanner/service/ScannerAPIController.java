package dbm.photo.scanner.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/scanner")
public class ScannerAPIController {

    private static final Logger log = LoggerFactory.getLogger(ScannerAPIController.class);

    @Autowired
    private PhotoRepository photos;

    @GetMapping("/greeting")
    public Photo greeting() { // HttpServletResponse response) {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        Optional<Photo> photo =  photos.findById("01f7a5f227d4b00bc76b002175725dcf");
        //photos.findOne
        return photo.isPresent() ? photo.get() : null;
        //return "{ \"a\": \"testing...\" }";
    }

    /*@GetMapping("/media")
    public void getMedia(HttpServletResponse response) {
        Optional<Photo> photo =  photos.findById("01f7a5f227d4b00bc76b002175725dcf");
        if (photo.isPresent()) {
            Photo p = photo.get();

            if (p.files.isEmpty()) {
                response.setStatus(404);
                return;
            }
            File file = new File(p.files.get(0));
            if (!file.exists()) {
                response.setStatus(404);
                return;
            }
            response.setContentType("image/jpeg");
            try {
                IOUtils.copy(new FileInputStream(file), response.getOutputStream());
            } catch (IOException e) {
                response.setStatus(404);
                return;
            }
        }
    } // */
}
