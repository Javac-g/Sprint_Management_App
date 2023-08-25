package com.softserve.edu.jom.repository;

import com.softserve.edu.jom.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    @Query("select p from Progress p join p.user.marathons maraphon where p.user.id =:userId and maraphon.id =:marathonId")
    List<Progress> findByUserIdAndMarathonId(@Param("userId") Long userId, @Param("marathonId") Long marathonId);

    @Query("select p from Progress p where p.user.id =:userId and p.task.sprint.id =:sprintId")
    List<Progress> findByUserIdAndSprintId(@Param("userId") Long userId, @Param("sprintId") Long sprintId);
}
