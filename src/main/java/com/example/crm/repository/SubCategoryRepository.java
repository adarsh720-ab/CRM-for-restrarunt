package com.example.crm.repository;

import com.example.crm.entity.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, UUID> {

    boolean existsByName(String name);

    // ✅ category (field in SubCategory) → categoryId (field in Category)
    List<SubCategory> findByCategoryCategoryId(UUID categoryId);
}