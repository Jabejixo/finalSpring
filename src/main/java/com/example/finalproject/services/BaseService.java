package com.example.finalproject.services;

import com.example.finalproject.models.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface BaseService<T extends BaseEntity> {
    T save(T entity);
    T update(T entity);
    T findById(UUID id);
    List<T> findAll();
    List<T> findByIsDeletedFalse();
    List<T> findByIsDeletedTrue();

    // Логическое удаление
    void softDelete(T entity);
    // Восстановление
    void restore(T entity);
    // Физическое удаление
    void delete(UUID id);
    // Пагинация
    Page<T> findAll(Pageable pageable);
    Page<T> findByIsDeletedFalse(Pageable pageable);
    Page<T> findByIsDeletedTrue(Pageable pageable);
    // Сортировка
    List<T> findAll(Sort sort);
    List<T> findByIsDeletedFalse(Sort sort);
    List<T> findByIsDeletedTrue(Sort sort);
    // Пагинация с сортировкой
    Page<T> findAll(Pageable pageable, Sort sort);
    Page<T> findAll(Pageable pageable, boolean isDeleted);
    Page<T> findByIsDeletedFalse(Pageable pageable, Sort sort);
    Page<T> findByIsDeletedTrue(Pageable pageable, Sort sort);
}
