package com.cwa.filemanagement.controller;

import com.cwa.filemanagement.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final FileService fileService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", fileService.getAllFiles());
        return "index";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "description", required = false) String description,
                                  RedirectAttributes redirectAttributes) {
        try {
            fileService.storeFile(file, description);
            redirectAttributes.addFlashAttribute("successMessage", "File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading file: " + e.getMessage());
        }
        return "redirect:/upload";
    }
}
