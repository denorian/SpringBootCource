package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.domain.dto.MessageDTO;
import com.example.sweater.repos.MessageRepo;
import com.example.sweater.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
	@Autowired
	private MessageRepo messageRepo;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private MessageService messageService;
	
	@GetMapping("/")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
						   Map<String, Object> model) {
		model.put("name", name);
		return "greeting";
	}
	
	@GetMapping("/main")
	public String main(
			@AuthenticationPrincipal User user,
			@RequestParam(required = false, defaultValue = "") String filter,
			Model model,
			@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<MessageDTO> page = messageService.messageList(pageable, filter, user);
		
		model.addAttribute("page", page);
		model.addAttribute("filter", filter);
		model.addAttribute("url", "/main");
		
		return "main";
	}
	
	@PostMapping("/main")
	public String add(
			@AuthenticationPrincipal User user,
			@Valid Message message,
			BindingResult bindingResult,
			@RequestParam("file") MultipartFile file,
			@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
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
		
		Page<MessageDTO> page = messageRepo.findAll(pageable, user);
		model.addAttribute("message", null);
		model.addAttribute("url", "/main");
		model.addAttribute("page", page);
		
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
	
	@GetMapping("/user-messages/{author}")
	public String userMessages(
			@AuthenticationPrincipal User currentUser,
			@PathVariable User author,
			Model model,
			@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(required = false) Message message
	) {
		Page<MessageDTO> page = messageService.messageListForUser(pageable, currentUser, author);
		model.addAttribute("userChannel", author);
		model.addAttribute("subscritionCount", author.getSubscruptions().size());
		model.addAttribute("subscribersCount", author.getSubscribers().size());
		model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
		model.addAttribute("page", page);
		model.addAttribute("message", message);
		model.addAttribute("isCurrentUser", author.equals(currentUser));
		model.addAttribute("url", "/user-messages/" + author.getId());
		
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
	
	@GetMapping("/messages/{message}/like")
	public String like(
			@AuthenticationPrincipal User currentUser,
			@PathVariable Message message,
			RedirectAttributes redirectAttributes,
			@RequestHeader(required = false) String referer
	) {
		Set<User> likes = message.getLikes();
		
		if(likes.contains(currentUser)){
			likes.remove(currentUser);
		}else {
			likes.add(currentUser);
		}
		
		UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
		
		components.getQueryParams()
				.entrySet()
				.forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
		
		return "redirect:" + components.getPath();
	}
}