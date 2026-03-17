package com.example.crm.repository;

import com.example.crm.entity.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Items, UUID> {
    List<Items> findBySubCategorySubCategoryId(UUID subCategoryId);

}
