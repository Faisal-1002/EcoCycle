package com.example.capstone03.Repository;

import com.example.capstone03.Model.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerRepository extends JpaRepository<Container,Integer> {

    Container findContainerById(Integer id);
    Container findTop1ByAvailableTrueOrderByIdAsc();
    List<Container> findAllByAvailableTrue();

}
