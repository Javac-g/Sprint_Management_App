package com.softserve.edu.jom.controller;

import com.softserve.edu.jom.exception.DuplicateUserEmailException;
import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.User;
import com.softserve.edu.jom.repository.RoleRepository;
import com.softserve.edu.jom.service.MarathonService;
import com.softserve.edu.jom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private UserService userService;
    private MarathonService marathonService;
    private RoleRepository roleRepository;

    @Autowired
    public StudentController(UserService userService, MarathonService marathonService, RoleRepository roleRepository) {
        this.userService = userService;
        this.marathonService = marathonService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/students")
    public String getUsers(Model model) {
        logger.info("Get all students");
        model.addAttribute("students", userService.getAllByRole("Trainee"));
        return "students";
    }

    
    @PostMapping("/addStudent")
    public String addStudent(Model model, @ModelAttribute(name = "student") @Valid User user, BindingResult bindingResult) {
        logger.info("Add new student %s %s", user.getFirstName(), user.getLastName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("mode", "Add");
            model.addAttribute("errorMessage", getErrorMessage(bindingResult));
            return "changeStudent";
        }
        userService.createOrUpdateUser(user);
        return "redirect:/students";

    }

   
    @GetMapping("/addStudent")
    public String showAddStudentPage(Model model) {
        logger.info("Show add new student page");
        User user = createNewStudent();
        model.addAttribute("user", user);
        model.addAttribute("mode", "Add");
        return "changeStudent";
    }

    
    @GetMapping("/students/{marathon_id}")
    public String getStudentsByMarathonId(@PathVariable(name = "marathon_id") Long marathon_id, Model model) {
        logger.info("Get all students for marathon with id=%d", marathon_id);
        checkMarathonExists(marathon_id);
        List<User> userList = userService.getAllByRoleAndMarathonId(User.Role.TRAINEE.name(), marathon_id);
        model.addAttribute("students", userList);
        model.addAttribute("marathonId", marathon_id);
        return "students";
    }

    
    @GetMapping("/students/{marathon_id}/delete/{student_id}")
    public String deleteStudentFromMarathon(@PathVariable(name = "marathon_id") Long marathon_id, @PathVariable(name = "student_id") Long student_id) {
        logger.info("Remove student with id=%d from marathon with id=%d", student_id, marathon_id);
        checkMarathonExists(marathon_id);
        checkStudentExists(student_id);
        if (userService.getStudentById(student_id) == null) {
            throw new EntityNotFoundException(String.format("Student with id=%d not found!", student_id));
        }
        userService.removeUserFromMarathon(student_id, marathon_id);
        return "redirect:/students/" + marathon_id;
    }

    
    @PostMapping("/students/{marathon_id}/add")
    public String saveStudentToMarathon(Model model, @ModelAttribute(name = "student") @Valid User user, BindingResult bindingResult,
                                        @PathVariable(name = "marathon_id") Long marathon_id) {
        logger.info("Add new student to marathon with id=%d", marathon_id);
        checkMarathonExists(marathon_id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("mode", "Add");
            model.addAttribute("errorMessage", getErrorMessage(bindingResult));
            return "changeStudent";
        }
        userService.addUserToMarathon(user, marathon_id);
        return "redirect:/students/" + marathon_id;
    }

    
    @GetMapping("/students/{marathon_id}/add")
    public String addStudentToMarathon(Model model, @PathVariable(name = "marathon_id") Long marathon_id) {
        logger.info("Show page to add new student to marathon with id=%d", marathon_id);
        checkMarathonExists(marathon_id);
        User user = createNewStudent();
        model.addAttribute("user", user);
        model.addAttribute("marathonId", marathon_id);
        model.addAttribute("mode", "Add");
        return "changeStudent";
    }

    
    @GetMapping("/students/{marathon_id}/edit/{student_id}")
    public String showEditStudentPage(Model model, @PathVariable(name = "marathon_id") Long marathon_id, @PathVariable(name = "student_id") Long student_id) {
        logger.info("Show edit student page for student with id=%d and marathon with id=%d", student_id, marathon_id);
        checkMarathonExists(marathon_id);
        User user = checkStudentExists(student_id);
        model.addAttribute("user", user);
        model.addAttribute("mode", "Edit");
        model.addAttribute("marathonId", marathon_id);
        return "changeStudent";
    }

    
    @PostMapping("/students/{marathon_id}/edit/{student_id}")
    public String editStudentPage(Model model, @PathVariable(name = "marathon_id") Long marathon_id,
                                  @PathVariable(name = "student_id") Long student_id,
                                  @ModelAttribute(name = "student") @Valid User user, BindingResult bindingResult) {
        logger.info("Edit student with id=%d and marathon with id=%d", student_id, marathon_id);
        checkMarathonExists(marathon_id);
        checkStudentExists(student_id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("mode", "Edit");
            model.addAttribute("errorMessage", getErrorMessage(bindingResult));
            return "changeStudent";
        }
        userService.createOrUpdateUser(user);
        return "redirect:/students/" + marathon_id;
    }

    private String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().map(i -> i.getDefaultMessage()).collect(Collectors.joining(";"));
    }

    private Marathon checkMarathonExists(Long marathon_id) {
        Marathon marathon = marathonService.getMarathonById(marathon_id);
        if (marathon == null) {
            throw new EntityNotFoundException(String.format("Marathon with id=%d not found!", marathon_id));
        }
        return marathon;
    }

    private User checkStudentExists(Long student_id) {
        User student = userService.getStudentById(student_id);
        if (student == null) {
            throw new EntityNotFoundException(String.format("Student with id=%d not found!", student_id));
        }
        return student;
    }

    private User createNewStudent() {
        User user = new User();
        user.setRole(roleRepository.getByRole(User.Role.TRAINEE));
        return user;
    }

    @ExceptionHandler(DuplicateUserEmailException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ModelAndView handleDuplicateUserEmailException(DuplicateUserEmailException ex, HttpServletRequest request) {
        logger.warn("Duplicate email exception raised. %s", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
