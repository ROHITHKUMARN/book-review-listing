package com.dotdash.recruiting.bookreview.server.controller;

import com.dotdash.recruiting.bookreview.server.BookReviewApplicationServer;
import com.dotdash.recruiting.bookreview.server.exception.GoodReadsException;
import com.dotdash.recruiting.bookreview.server.model.Book;
import com.dotdash.recruiting.bookreview.server.service.BookReviewService;
import com.dotdash.recruiting.bookreview.server.service.BookReviewServiceImpl;
import com.dotdash.recruiting.bookreview.server.utils.HttpClientFactory;
import com.dotdash.recruiting.bookreview.server.utils.HttpClientFactoryImpl;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BookReviewApplicationServer.class, GoodReadsException.class,
        Book.class, BookReviewService.class, BookReviewServiceImpl.class, HttpClientFactory.class,
        HttpClientFactoryImpl.class, BookReviewServerController.class})
@WebMvcTest(BookReviewServerController.class)
public class BookReviewServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BookReviewService bookService;

    @MockBean
    Book book;

    @MockBean
    HttpClientFactoryImpl httpClientFactory;

    @Test
    public void getBookListByAuthor_SUCCESS() throws Exception {
        String expectedJson = "[{\"author\":\"X\",\"title\":\"B\",\"url\":\"http://x.png\"},{\"author\":\"Y\",\"title\":\"A\",\"url\":\"http://y.png\"}]";
        List<Book> bookList = Lists.newArrayList(
                Book.builder().author("X").title("B").url("http://x.png").build(),
                Book.builder().author("Y").title("A").url("http://y.png").build()
        );
        when(bookService.getBooksList("example")).thenReturn(bookList);
        this.mockMvc.perform(get("/api/books?search=example")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getBookList_sortedByAuthor() throws Exception {
        String expectedJson = "[{\"author\":\"X\",\"title\":\"B\",\"url\":\"http://x.png\"},{\"author\":\"Y\",\"title\":\"A\",\"url\":\"http://y.png\"}]";
        List<Book> bookList = Lists.newArrayList(
                Book.builder().author("X").title("B").url("http://x.png").build(),
                Book.builder().author("Y").title("A").url("http://y.png").build()
        );
        List<Book> bookListSortedByAuthor = Lists.newArrayList(
                Book.builder().author("X").title("B").url("http://x.png").build(),
                Book.builder().author("Y").title("A").url("http://y.png").build()
        );
        when(bookService.getBooksList("example")).thenReturn(bookList);
        when(bookService.getBooksListByAuthor(bookList)).thenReturn(bookListSortedByAuthor);
        this.mockMvc.perform(get("/api/books?search=example&sortByAuthor=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getBookList_sortedByTitle() throws Exception {
        String expectedJson = "[{\"author\":\"Y\",\"title\":\"A\",\"url\":\"http://y.png\"},{\"author\":\"X\",\"title\":\"B\",\"url\":\"http://x.png\"}]";
        List<Book> bookList = Lists.newArrayList(
                Book.builder().author("X").title("B").url("http://x.png").build(),
                Book.builder().author("Y").title("A").url("http://y.png").build()
        );
        List<Book> bookListSortedByTitle = Lists.newArrayList(
                Book.builder().author("Y").title("A").url("http://y.png").build(),
                Book.builder().author("X").title("B").url("http://x.png").build()
        );
        when(bookService.getBooksList("example")).thenReturn(bookList);
        when(bookService.getBooksListByTitle(bookList)).thenReturn(bookListSortedByTitle);
        this.mockMvc.perform(get("/api/books?search=example&sortByTitle=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getBookList_paginated() throws Exception {
        String expectedJson = "[{\"author\":\"X\",\"title\":\"B\",\"url\":\"http://x.png\"}]";
        List<Book> bookList = Lists.newArrayList(
                Book.builder().author("X").title("B").url("http://x.png").build(),
                Book.builder().author("Y").title("A").url("http://y.png").build()
        );
        List<Book> bookListSortedByTitle = Lists.newArrayList(
                Book.builder().author("X").title("B").url("http://x.png").build()
        );
        when(bookService.getBooksList("example")).thenReturn(bookList);
        when(bookService.getPaginatedBooksList(bookList,1,1)).thenReturn(bookListSortedByTitle);
        this.mockMvc.perform(get("/api/books?search=example&page=1&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getBookListByAuthor_Error() throws Exception {
        when(bookService.getBooksList("example")).thenReturn(null);
        this.mockMvc.perform(get("/api/books?search=example")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

}