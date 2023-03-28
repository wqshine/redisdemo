package com.example.redisdemo.repo;

import com.example.redisdemo.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    @EntityGraph(attributePaths="roles")
    User findByUserNameAndDeletedFalse(String userName);
    long countByAdminTrueAndDeletedFalse();
}
