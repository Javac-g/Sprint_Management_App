package com.softserve.edu.jom.repository;

import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarathonRepository extends JpaRepository<Marathon, Long> {
    Marathon getMarathonById(Long id);

    @Query("select m from Marathon m join m.users user where user.id =:userId")
    List<Marathon> findByUserId(@Param("userId") Long userId);

    void deleteMarathonById(Long id);
}
