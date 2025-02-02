package com.cottongallery.backend.item.repository;

import com.cottongallery.backend.item.domain.Item;
import com.cottongallery.backend.item.constants.ItemStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @EntityGraph(attributePaths = "discount")
    Slice<Item> findAllByItemStatus(Pageable pageable, ItemStatus itemStatus);

    @EntityGraph(attributePaths = "discount")
    Slice<Item> findAllByItemStatusAndNameContaining(Pageable pageable, ItemStatus itemStatus, String keyword);

    boolean existsByItemImagePath(String itemImagePath);
    boolean existsByItemInfoImagePath(String itemImagePath);
}
