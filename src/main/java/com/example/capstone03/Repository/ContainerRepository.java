package com.example.capstone03.Repository;

import com.example.capstone03.Model.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContainerRepository extends JpaRepository<Container,Integer> {

    Container findContainerById(Integer id);
    @Query("SELECT c FROM Container c WHERE c.is_available = true ORDER BY c.id ASC")
    Container findFirstAvailableContainer();

}
