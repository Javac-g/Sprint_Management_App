package com.softserve.edu.jom.controller;

import com.softserve.edu.jom.exception.MarathonIsNotEmptyException;
import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.User;
import com.softserve.edu.jom.service.MarathonService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Data
public class MarathonController {
    private MarathonService marathonService;

    @Autowired
    public void setMarathonService(MarathonService marathonService) {
        this.marathonService = marathonService;
    }

    @GetMapping("/marathons")
    public String getAllMarathons(Model model) {
        log.info("Get all marathons");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<Marathon> marathons;
        if (roles.size() == 1 && roles.iterator().next() == User.Role.TRAINEE) {
            marathons = marathonService.getMarathonsByUserId(user.getId());
        } else {
            marathons = marathonService.getAll();
        }
        model.addAttribute("marathons", marathons);
        return "marathons";
    }

    @GetMapping("/delete/{marathonId}")
    public String deleteMarathon(@PathVariable(name = "marathonId") Long id) {
        log.info(String.format("delete marathon with id %d", id));
        if (marathonService.getMarathonById(id) == null) {
            throw new EntityNotFoundException(String.format("Marathon with id=%d not found!", id));
        }
        marathonService.deleteMarathonById(id);
        log.info(String.format("Marathon id = %d deleted successfully", id));
        return "redirect:/marathons";
    }

    @GetMapping("/edit/{marathonId}")
    public String editMarathon(Model model, @PathVariable(name = "marathonId") Long id) {
        log.info(String.format("Edit marathon with id %d", id));
        if (marathonService.getMarathonById(id) == null) {
            throw new EntityNotFoundException(String.format("Marathon with id=%d not found!", id));
        }
        Marathon marathon = marathonService.getMarathonById(id);
        model.addAttribute("marathon", marathon);
        model.addAttribute("mode", "Edit");
        return "changeMarathon";
    }

    @PostMapping("/edit/{marathonId}")
    public String editMarathon(Model model, @PathVariable(name = "marathonId") Long id,
                               @ModelAttribute(name = "marathon") @Valid Marathon marathon,
                               BindingResult bindingResult) {
        log.info("Edit marathon with id=%d", id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("marathon", marathon);
            model.addAttribute("mode", "Edit");
            model.addAttribute("errorMessage", getErrorMessage(bindingResult));
            return "changeMarathon";
        }
        marathon.setId(id);
        marathonService.createOrUpdate(marathon);
        log.info(String.format("Marathon with id %d title updated to %s", id, marathon.getTitle()));
        return "redirect:/marathons";
    }


    @GetMapping("/addMarathon")
    public String addMarathon(Model model) {
        log.info("Add marathon");
        Marathon marathon = new Marathon();
        model.addAttribute("marathon", marathon);
        model.addAttribute("mode", "Add");
        return "changeMarathon";
    }

    @PostMapping("/addMarathon")
    public String addMarathon(Model model, @ModelAttribute(name = "marathon") @Valid Marathon marathon,
                              BindingResult bindingResult) {
        log.info(String.format("Add %s marathon", marathon.getTitle()));
        if (bindingResult.hasErrors()) {
            model.addAttribute("marathon", marathon);
            model.addAttribute("mode", "Add");
            model.addAttribute("errorMessage", getErrorMessage(bindingResult));
            return "changeMarathon";
        }
        marathonService.createOrUpdate(marathon);
        return "redirect:/marathons";
    }

    @ExceptionHandler(MarathonIsNotEmptyException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ModelAndView handleMarathonNotEmptyException(MarathonIsNotEmptyException ex) {
        log.warn(String.format("Marathon is not empty exception. %s", ex.getMessage()));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    private String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
    }
}
