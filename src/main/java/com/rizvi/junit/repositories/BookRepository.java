package com.rizvi.junit.repositories;

import com.rizvi.junit.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {


}
