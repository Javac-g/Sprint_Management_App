package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.Sprint;
import com.softserve.edu.jom.repository.SprintRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintServiceImpl implements SprintService {

    private SprintRepository sprintRepository;

    @Autowired
    public SprintServiceImpl(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    @Override
    public List<Sprint> getSprintsByMarathonId(Long id) {
        return sprintRepository.getSprintsByMarathonId(id);
    }

    @Override
    public Sprint getSprintById(Long id) {
        return sprintRepository.getSprintById(id);
    }

    @Override
    public boolean addSprintToMarathon(Sprint sprint, Marathon marathon) {
        Sprint newSprint = new Sprint();
        Validate.notNull(sprint.getTitle(), "Title must be not null");
        Validate.notNull(sprint.getStart(), "Start date must be not null");
        Validate.notNull(sprint.getFinish(), "Finish date must be not null");

        newSprint.setTitle(sprint.getTitle());
        newSprint.setMarathon(marathon);
        newSprint.setStart(sprint.getStart());
        newSprint.setFinish(sprint.getFinish());

        return sprintRepository.save(newSprint) != null;
    }


    @Override
    public boolean updateSprint(Sprint sprint) {
        Sprint newSprint = new Sprint();

        Validate.notNull(sprint.getTitle(), "Title must be not null");
        Validate.notNull(sprint.getStart(), "Start date must be not null");
        Validate.notNull(sprint.getFinish(), "Finish date must be not null");
        newSprint.setStart(sprint.getStart());
        newSprint.setFinish(sprint.getFinish());
        newSprint.setTitle(sprint.getTitle());
        if (sprint.getMarathon() != null) {
            newSprint.setMarathon(sprint.getMarathon());
        }

        return sprintRepository.save(newSprint) != null;
    }
}
