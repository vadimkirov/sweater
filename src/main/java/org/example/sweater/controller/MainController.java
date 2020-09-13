package org.example.sweater.controller;

import org.example.sweater.domain.Message;
import org.example.sweater.domain.User;
import org.example.sweater.repository.MessageRepo;
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
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model){
        Iterable<Message> allMessages = messageRepo.findAll();
        model.put("messages", allMessages);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam ("file") MultipartFile file,
            @Valid Message message,
            BindingResult bindingResult,
            Model model) throws IOException {
        message.setAuthor(user);

        if(bindingResult.hasFieldErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        }else {
            model.addAttribute("message", null);
            saveFile(file, message);
            messageRepo.save(message);
        }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    private void saveFile(@RequestParam("file") MultipartFile file, @Valid Message message) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter,
                         Map<String, Object> model){
        Iterable<Message> byTag;
        if(filter != null && !filter.isEmpty() ){
            byTag = messageRepo.findByTag(filter);
        }else {
            byTag = messageRepo.findAll();
        }
        model.put("messages", byTag);
        return "main";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @RequestParam(required = false) Message message,
            Model model
    ){
        Set<Message> messages = user.getMessages();

        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));



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
        if(message.getAuthor().equals(currentUser)){
            if(!StringUtils.isEmpty(text)){
                message.setText(text);
            }
            if(!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }

        }
        saveFile(file,message);
        messageRepo.save(message);
        return "redirect:/user-messages/" + user;
    }

}