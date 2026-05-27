package com.hiimtuan.post_service.repository;

import com.hiimtuan.post_service.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAll(Pageable pageable);
    List<Post> findByAuthorId(Long authorId, Pageable pageable);
}
