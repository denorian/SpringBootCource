package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
	@Autowired
	private MessageRepo messageRepo;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@GetMapping("/")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
						   Map<String, Object> model) {
		model.put("name", name);
		return "greeting";
	}
	
	@GetMapping("/main")
	public String main(@RequestParam(required = false) String filter, Model model) {
		Iterable<Message> messages;
		if (filter == null || filter.isEmpty())
			messages = messageRepo.findAll();
		else
			messages = messageRepo.findByTag(filter);
		
		model.addAttribute("messages", messages);
		model.addAttribute("filter", (filter == null || filter.isEmpty()) ? "" : filter);
		
		return "main";
	}
	
	@PostMapping("/main")
	public String add(
			@AuthenticationPrincipal User user,
			@RequestParam String text,
			@RequestParam String tag,
			@RequestParam("file") MultipartFile file,
			Map<String, Object> model) throws IOException {
		
		Message message = new Message(text, tag, user);
		
		if (file != null && !file.getOriginalFilename().isEmpty()) {
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			String uuidFile = UUID.randomUUID().toString();
			String resultNameFile = uuidFile + '.' + file.getOriginalFilename();
			
			file.transferTo(new File(uploadPath + "/" + resultNameFile));
			
			message.setFilename(resultNameFile);
		}
		
		messageRepo.save(message);
		Iterable<Message> messages = messageRepo.findAll();
		model.put("messages", messages);
		
		return "main";
	}
}