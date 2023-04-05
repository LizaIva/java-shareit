package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>,
        PagingAndSortingRepository<Item, Integer> {

    Page<Item> findByOwnerIdOrderById(@Param("ownerId") Integer ownerId, Pageable pageable);

    List<Item> findByOwnerIdOrderById(@Param("ownerId") Integer ownerId);


    @Query("select i from items i " +
            "where i.available is true " +
            "   and (lower(i.name) like concat('%', :textValue, '%') or lower(i.description) like concat('%', :textValue, '%'))")
    Page<Item> foundAvailableItemWithNameOrDescription(@Param("textValue") String textValue, Pageable pageable);

    @Query("select i from items i " +
            "where i.available is true " +
            "   and (lower(i.name) like concat('%', :textValue, '%') or lower(i.description) like concat('%', :textValue, '%'))")
    List<Item> foundAvailableItemWithNameOrDescription(@Param("textValue") String textValue);
}