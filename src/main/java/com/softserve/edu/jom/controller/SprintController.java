package com.softserve.edu.jom.controller;

import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.Sprint;
import com.softserve.edu.jom.service.MarathonService;
import com.softserve.edu.jom.service.SprintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Controller
public class SprintController {
    private SprintService sprintService;
    private MarathonService marathonService;

    @GetMapping("/sprints/{marathonId}")
    public String getSprintForMarathon(Model model, @PathVariable(name = "marathonId") Long id) {
        log.info(String.format("Getting sprints for marathon with id %d", id));
        Marathon marathon = marathonService.getMarathonById(id);
        if (marathon == null) {
            throw new EntityNotFoundException(String.format("Marathon with id=%d not found!", id));
        }
        List<Sprint> sprintList = sprintService.getSprintsByMarathonId(id);
        model.addAttribute("sprints", sprintList);
        model.addAttribute("marathonTitle", marathon.getTitle());
        return "sprints";
    }

    @Autowired
    public void setSprintService(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @Autowired
    public void setMarathonService(MarathonService marathonService) {
        this.marathonService = marathonService;
    }
}
