package com.decagon.fitnessoapp.repository;

import com.decagon.fitnessoapp.model.blog.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {


}
