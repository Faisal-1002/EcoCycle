package com.example.capstone03.Repository;

import com.example.capstone03.Model.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface ContainerRepository extends JpaRepository<Container,Integer> {

    Container findContainerById(Integer id);

    Container findTopByAvailableTrue();//ماتشتغل

}
