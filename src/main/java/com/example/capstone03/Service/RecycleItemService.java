package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.RecycleItem;
import com.example.capstone03.Repository.RecycleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecycleItemService {

    private final RecycleItemRepository recycleItemRepository;

    public List<RecycleItem> getAllRecycleItems() {
        return recycleItemRepository.findAll();
    }

    public void addRecycleItem(RecycleItem recycleItem) {
        recycleItemRepository.save(recycleItem);
    }

    public void updateRecycleItem(Integer id, RecycleItem updatedRecycleItem) {
        RecycleItem recycleItem = recycleItemRepository.findRecycleItemById(id);
        if (recycleItem == null) {
            throw new ApiException("Recycle item not found");
        }

        recycleItem.setPickup_request(updatedRecycleItem.getPickup_request());
        recycleItem.setType(updatedRecycleItem.getType());
        recycleItem.setWeight_kg(updatedRecycleItem.getWeight_kg());

        recycleItemRepository.save(recycleItem);
    }

    public void deleteRecycleItem(Integer id) {
        RecycleItem recycleItem = recycleItemRepository.findRecycleItemById(id);
        if (recycleItem == null) {
            throw new ApiException("Recycle item not found");
        }

        recycleItemRepository.delete(recycleItem);
    }

    public RecycleItem getRecycleItemById(Integer id) {
        RecycleItem recycleItem = recycleItemRepository.findRecycleItemById(id);
        if (recycleItem == null) {
            throw new ApiException("Recycle item not found");
        }
        return recycleItem;
    }
}
