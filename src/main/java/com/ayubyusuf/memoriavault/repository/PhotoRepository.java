package com.ayubyusuf.memoriavault.repository;

import com.ayubyusuf.memoriavault.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
