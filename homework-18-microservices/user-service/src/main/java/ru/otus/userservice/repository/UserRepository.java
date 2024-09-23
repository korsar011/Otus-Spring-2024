package ru.otus.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.userservice.model.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}