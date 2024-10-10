package com.example.finalproject.services;
import com.example.finalproject.models.BaseEntity;
import com.example.finalproject.repositories.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity, R extends BaseRepository<T>> implements BaseService<T> {
    protected final R repository;

    public BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(T entity) {
        return repository.save(entity);
    }

    @Override
    public T findById(UUID id) {
        Optional<T> entity = repository.findById(id);
        return entity.orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findByIsDeletedFalse() {
        return repository.findAllByIsDeletedFalse();
    }

    @Override
    public List<T> findByIsDeletedTrue() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void softDelete(T entity) {
        entity.setIsDeleted(true);
        repository.save(entity);
    }

    @Override
    public void restore(T entity) {
        entity.setIsDeleted(false);
        repository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        repository.hardDelete(id); // Физическое удаление
    }

    // Методы для работы с пагинацией
    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    @Override
    public Page<T> findAll(Pageable pageable, boolean isDeleted) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<T> findByIsDeletedFalse(Pageable pageable) {
        return repository.findAllByIsDeletedFalse(pageable);
    }

    @Override
    public Page<T> findByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    // Методы для работы с сортировкой
    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public List<T> findByIsDeletedFalse(Sort sort) {
        return repository.findAllByIsDeletedFalse(sort);
    }

    @Override
    public List<T> findByIsDeletedTrue(Sort sort) {
        return repository.findAllByIsDeletedTrue(sort);
    }

    // Методы для работы с пагинацией и сортировкой
    @Override
    public Page<T> findAll(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAll(pageableWithSort);
    }

    @Override
    public Page<T> findByIsDeletedFalse(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByIsDeletedFalse(pageableWithSort);
    }

    @Override
    public Page<T> findByIsDeletedTrue(Pageable pageable, Sort sort) {
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByIsDeletedTrue(pageableWithSort);
    }
}
