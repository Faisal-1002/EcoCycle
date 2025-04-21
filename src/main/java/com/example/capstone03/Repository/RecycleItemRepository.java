package com.example.capstone03.Repository;

import com.example.capstone03.Model.RecycleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecycleItemRepository extends JpaRepository<RecycleItem, Integer> {
    RecycleItem findRecycleItemById(Integer id);
}
