package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.Sprint;

import java.util.List;

public interface SprintService {
    List<Sprint> getSprintsByMarathonId(Long id);
    boolean addSprintToMarathon(Sprint sprint, Marathon marathon);
    Sprint getSprintById(Long id);
    boolean updateSprint(Sprint sprint);

}
