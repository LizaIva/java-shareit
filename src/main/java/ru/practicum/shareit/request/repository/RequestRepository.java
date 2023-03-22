package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@Repository
public interface RequestRepository
        extends JpaRepository<Request, Integer>,
        PagingAndSortingRepository<Request, Integer> {

    @Query("from requests r where r.requestor.id <> :userId ")
    Page<Request> findAlByUserIdlWithOffset(@Param("userId") Integer userId, Pageable pageable);
}
