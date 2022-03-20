package com.rizvi.junit.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rizvi.junit.domain.Book;
import com.rizvi.junit.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.junit.jupiter.MockitoExtension;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BookControllerTest {



    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

        Book Record_1 = new Book(1L, "Atomic Habits", "How to build better Habits", 5);
        Book Record_2 = new Book(2L, "Thinking Fast and Slow", "How to create better mental modes", 4);
        Book Record_3 = new Book(3L, "Grokking Algorithms", "How to learn Algorithms easy Way ", 2);


    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllBookRecords() throws  Exception {

        //given
        List<Book> records = new ArrayList<>(Arrays.asList(Record_1, Record_2, Record_3));

        //when

        Mockito.when(bookRepository.findAll()).thenReturn(records);


        mockMvc.perform(MockMvcRequestBuilders
                .get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name" , is("Grokking Algorithms")))
                .andExpect(jsonPath("$[1].name" , is("Thinking Fast and Slow")));

    }

    @Test
    public void getBookById() throws Exception{

        Mockito.when(bookRepository.findById(Record_1.getBookId())).thenReturn(Optional.ofNullable(Record_1));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name" , is("Atomic Habits")));

    }

    @Test
    public void createBookRecord () throws Exception{

        Book record = Book.builder()
                .bookId(4L)
                .name("Introduction to Java")
                .summary("This Book is on Core Java")
                .rating(5)
                .build();


        Mockito.when(bookRepository.save(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);


        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name" , is("Introduction to Java")));





    }

    @Test
    public void updateBookRecord() throws Exception {

        Book updateRecord = Book.builder()
                .bookId(1L)
                .name("Updated Book Name")
                .summary("new Updated  summary")
                .rating(10).build();


        Mockito.when(bookRepository.findById(Record_1.getBookId())).thenReturn(Optional.ofNullable(Record_1));
        Mockito.when(bookRepository.save(updateRecord)).thenReturn(updateRecord);


        String updatedContent = objectWriter.writeValueAsString(updateRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);


        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Updated Book Name")));


      }

        @Test
        public void deleteBookById_success() throws Exception {

         Mockito.when(bookRepository.findById(Record_2.getBookId())).thenReturn(Optional.of(Record_2));

         mockMvc.perform(MockMvcRequestBuilders
                 .delete("/book/2")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk());


    }
}