package com.dotdash.recruiting.bookreview.server;

import com.dotdash.recruiting.bookreview.server.controller.BookReviewServerController;
import com.dotdash.recruiting.bookreview.server.exception.GoodReadsException;
import com.dotdash.recruiting.bookreview.server.model.Book;
import com.dotdash.recruiting.bookreview.server.service.BookReviewService;
import com.dotdash.recruiting.bookreview.server.service.BookReviewServiceImpl;
import com.dotdash.recruiting.bookreview.server.utils.HttpClientFactory;
import com.dotdash.recruiting.bookreview.server.utils.HttpClientFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BookReviewApplicationServer.class, GoodReadsException.class,
        Book.class, BookReviewService.class, BookReviewServiceImpl.class, HttpClientFactory.class,
        HttpClientFactoryImpl.class, BookReviewServerController.class})

@WebMvcTest(BookReviewServerController.class)
public class BookReviewApplicationServerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BookReviewService bookService;

    @MockBean
    Book book;

    @MockBean
    HttpClientFactoryImpl httpClientFactory;

    @MockBean
    private BookReviewServerController bookReviewServerController;

    @Test
    public void contextLoads() {
        assertThat(bookReviewServerController).isNotNull();
    }

    @Test public void main(){  BookReviewApplicationServer.main(new String[] {});}

}
