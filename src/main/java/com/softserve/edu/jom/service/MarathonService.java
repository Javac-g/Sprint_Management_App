package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Marathon;

import java.util.List;

public interface MarathonService {
    Marathon getMarathonById(Long id);

    List<Marathon> getMarathonsByUserId(Long id);

    Marathon createOrUpdate(Marathon marathon);

    List<Marathon> getAll();

    void deleteMarathonById(Long id);
}
