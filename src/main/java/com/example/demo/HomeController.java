package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserService userService;

  @RequestMapping("/")
  public String index(){
    return "index";
  }

  @RequestMapping("/in")
  public String listCourses(Model model){
    model.addAttribute("courses", courseRepository.findAll());
    return "list";
  }

  @GetMapping("/add")
  public String courseForm(Model model) {
    model.addAttribute("course", new Course());
    return "courseform";
  }

  @PostMapping("/process")
  public String processForm(@Valid Course course, BindingResult result){
    if(result.hasErrors()){
      return "courseform";
    }

    courseRepository.save(course);
    return "redirect:/";
  }
//
//    @Autowired
//    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
      model.addAttribute("user", new User());
      return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid
                                          @ModelAttribute("user") User user, BindingResult result, Model model) {
      model.addAttribute("user", user);
      if (result.hasErrors())
      {
        return "registration";
      } else {
        userService.saveUser(user);
        model.addAttribute("message", "User Account Created");
      }
      return "index";
    }

    @RequestMapping("/login")
    public String login(){
      return "login";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model){
      User myuser = ((CustomUserDetails)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUser();
      model.addAttribute("myuser", myuser);
      return "secure";
    }

  @RequestMapping("/detail/{id}")
  public String showCourse(@PathVariable("id") long id, Model model){
    model.addAttribute("course", courseRepository.findById(id).get());
    return "show";
  }

  @RequestMapping("/update/{id}")
  public String updateCourse(@PathVariable("id") long id, Model model){
    model.addAttribute("course", courseRepository.findById(id).get());
    return "courseform";
  }

  @RequestMapping("/delete/{id}")
  public String delCourse(@PathVariable("id") long id){
    courseRepository.deleteById(id);
    return "redirect:/";
  }

//  @PostMapping("/user")
//  public String userForm(@Valid User user, BindingResult result){
//    if(result.hasErrors()){
//      return "courseform";
//    }
//
//    userRepository.save(user);
//    return "redirect:/";
//  }

protected User getUser(){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String currentusername = authentication.getName();
      //call the method!
      User user= userRepository.findByUsername(currentusername);
      return user;
}
//for 1 user only
  //associate user with the course
}
