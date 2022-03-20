package com.rizvi.junit.Controllers;


import com.rizvi.junit.domain.Book;
import com.rizvi.junit.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value= "/book")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBookRecords(){
        return bookRepository.findAll();
    }

    @GetMapping(value = "{bookId}")
    public Book getBookById(@PathVariable(value="bookId") Long bookId){
        return bookRepository.findById(bookId).get();
    }

    @PostMapping
    public Book createBookRecord(@RequestBody @Valid Book bookRecord){
        return bookRepository.save(bookRecord);
    }

    @PutMapping
    public Book updateBookRecord(@RequestBody @Valid Book bookRecord) throws NotFoundException {
        if(bookRecord == null  || bookRecord.getBookId() == null){
            throw new RuntimeException("Book Record ");
        }
        Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());
        if(!optionalBook.isPresent()) {
            throw new RuntimeException("Book with Id   "+ bookRecord.getBookId()+ " Does not  exist");
        }
        Book existingBookRecord = optionalBook.get();
        existingBookRecord.setName(bookRecord.getName());
        existingBookRecord.setSummary(bookRecord.getSummary());
        existingBookRecord.setRating(bookRecord.getRating());
        return bookRepository.save(existingBookRecord);
    }



    @DeleteMapping(value = "{bookId}")
    public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws NotFoundException {

        if(!bookRepository.findById(bookId).isPresent()){
            throw new RuntimeException("bookId  "+ bookId + "not present");
        }
        bookRepository.deleteById(bookId);
    }

}
