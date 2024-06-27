package com.bloomberg.fxdeals.dao;

import com.bloomberg.fxdeals.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepo extends JpaRepository<Deal,String> {
    boolean existsByDealUniqueId(String dealUniqueId);
}
