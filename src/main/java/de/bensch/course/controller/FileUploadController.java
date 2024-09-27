package de.bensch.course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static de.bensch.course.controller.UrlMappings.STUDENT_UPLOAD;

@Controller
public class FileUploadController {

    @ModelAttribute("urlMappings")
    public UrlMappings urlMappings() {
        return new UrlMappings();
    }

    @PostMapping(STUDENT_UPLOAD)
    public String uploadStudents(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        //   try {
        // Save the file to the server
        // byte[] bytes = file.getBytes();


        redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");

//        } catch (IOException e) {
//            log.warn("Error while upload",e);
//        }

        return "redirect:/uploadStatus";

    }

    // Handle the upload status page
    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
