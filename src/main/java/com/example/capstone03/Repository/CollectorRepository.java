package com.example.capstone03.Repository;

import com.example.capstone03.Model.Collector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectorRepository extends JpaRepository<Collector,Integer> {

    Collector findCollectorById(Integer id);
}
