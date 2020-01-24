package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
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
			@Valid Message message,
			BindingResult bindingResult,
			@RequestParam("file") MultipartFile file,
			Model model) throws IOException {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute(message);
			Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
			model.mergeAttributes(errorMap);
			
		} else {
			message.setAuthor(user);
			
			saveFile(message, file);
			
			messageRepo.save(message);
		}
		
		Iterable<Message> messages = messageRepo.findAll();
		model.addAttribute("message", null);
		model.addAttribute("messages", messages);
		
		return "main";
	}
	
	private void saveFile(Message message, MultipartFile file) throws IOException {
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
	}
	
	@GetMapping("/user-messages/{user}")
	public String userMessages(
			@AuthenticationPrincipal User currentUser,
			@PathVariable User user,
			Model model,
			@RequestParam(required = false) Message message
	) {
		
		Set<Message> messages = user.getMessages();
		
		model.addAttribute("userChannel", user);
		model.addAttribute("subscritionCount", user.getSubscruptions().size());
		model.addAttribute("subscribersCount", user.getSubscribers().size());
		model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
		model.addAttribute("messages", messages);
		model.addAttribute("message", message);
		model.addAttribute("isCurrentUser", user.equals(currentUser));
		
		return "userMessages";
	}
	
	
	@PostMapping("/user-messages/{user}")
	public String updateMessage(
			@AuthenticationPrincipal User currentUser,
			@PathVariable Long user,
			@RequestParam("id") Message message,
			@RequestParam("text") String text,
			@RequestParam("tag") String tag,
			@RequestParam("file") MultipartFile file
	) throws IOException {
		if (message.getAuthor().equals(currentUser)) {
			if (!StringUtils.isEmpty(text)) {
				message.setText(text);
			}
			
			if (!StringUtils.isEmpty(tag)) {
				message.setTag(tag);
			}
			
			saveFile(message, file);
			
			messageRepo.save(message);
		}
		
		return "redirect:/user-messages/" + user;
	}
}