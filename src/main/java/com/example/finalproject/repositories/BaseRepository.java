package com.example.finalproject.repositories;

import com.example.finalproject.models.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RepositoryRestResource(exported = false)
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
    // Найти все сущности, которые не были логически удалены
    List<T> findAllByIsDeletedFalse();
    // Найти все сущности с логическим удалением, с сортировкой
    List<T> findAllByIsDeletedFalse(Sort sort);
    // Пагинация для сущностей без логического удаления
    Page<T> findAllByIsDeletedFalse(Pageable pageable);
    // Найти все удалённые сущности
    List<T> findAllByIsDeletedTrue();
    // Пагинация для удалённых сущностей
    Page<T> findAllByIsDeletedTrue(Pageable pageable);
    // Сортировка для удалённых сущностей
    List<T> findAllByIsDeletedTrue(Sort sort);
    // Логическое удаление сущности по ID
    default void softDelete(UUID id) {
        Optional<T> entity = findById(id);
        if (entity.isPresent()) {
            entity.get().setIsDeleted(true);
            save(entity.get());
        }
    }
    // Физическое удаление сущности по ID
    default void hardDelete(UUID id) {
        deleteById(id);
    }
    // Логическое удаление списка сущностей
    default void softDeleteAll(List<UUID> ids) {
        List<T> entities = findAllById(ids);
        entities.forEach(entity -> entity.setIsDeleted(true));
        saveAll(entities);
    }
    // Физическое удаление списка сущностей
    default void hardDeleteAll(List<UUID> ids) {
        deleteAllById(ids);
    }
    // Восстановление логически удалённой сущности
    default void restore(UUID id) {
        Optional<T> entity = findById(id);
        if (entity.isPresent() && entity.get().getIsDeleted()) {
            entity.get().setIsDeleted(false);
            save(entity.get());
        }
    }
    // Восстановление нескольких логически удалённых сущностей
    default void restoreAll(List<UUID> ids) {
        List<T> entities = findAllById(ids);
        entities.forEach(entity -> entity.setIsDeleted(false));
        saveAll(entities);
    }
    // Частичное обновление сущности (путём передачи действия с изменениями)
    default void partialUpdate(UUID id, Action<T> updateAction) {
        Optional<T> entity = findById(id);
        if (entity.isPresent()) {
            T existingEntity = entity.get();
            updateAction.apply(existingEntity);
            if (!existingEntity.equals(entity.get())) { // Проверяем, были ли изменения
                save(existingEntity);
            }
        }
    }
    // Интерфейс для применения частичных изменений
    interface Action<T> {
        void apply(T entity);
    }
}
