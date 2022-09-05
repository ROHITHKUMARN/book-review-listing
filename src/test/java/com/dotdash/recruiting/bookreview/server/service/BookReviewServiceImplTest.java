package com.dotdash.recruiting.bookreview.server.service;

import com.dotdash.recruiting.bookreview.server.exception.GoodReadsException;
import com.dotdash.recruiting.bookreview.server.model.Book;
import com.dotdash.recruiting.bookreview.server.utils.HttpClientFactoryImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookReviewServiceImplTest {

    private static final String BASE_URL = "https://www.goodreads.com/search.xml?key=RDfV4oPehM6jNhxfNQzzQ";
    private static final String QUERY_URL = BASE_URL + "&q=";


    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    HttpGet mockHttpGet = mock(HttpGet.class);
    StatusLine mockStatusLine = mock(StatusLine.class);
    CloseableHttpResponse mockCloseableHttpResponse = mock(CloseableHttpResponse.class);
    CloseableHttpClient mockCloseableHttpClient = mock(CloseableHttpClient.class);
    HttpEntity mockHttpEntity = mock(HttpEntity.class);
    BookReviewServiceImpl bookReviewService = new BookReviewServiceImpl();

    @InjectMocks
    HttpClientFactoryImpl mockHttpClientFactory;

    private List<Book> bookList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getXMLResponseFromGoodReadsAPI_OK() {
        assertNotNull(bookReviewService.getXMLResponseFromGoodReadsAPI(QUERY_URL+"lion"));
    }

    @Test
    public void getXMLResponseFromGoodReadsAPI_EmptyUrl() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid Good Reads URL");
        bookReviewService.getXMLResponseFromGoodReadsAPI("");
    }

    @Test
    public void getXMLResponseFromGoodReadsAPI_BlankUrl() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid Good Reads URL");
        bookReviewService.getXMLResponseFromGoodReadsAPI(" ");
    }

    @Test
    public void getXMLResponseFromGoodReadsAPI_NullUrl() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid Good Reads URL");
        bookReviewService.getXMLResponseFromGoodReadsAPI(null);
    }

    @Test
    public void getXMLResponseFromGoodReadsAPI_InvalidStatus() throws IOException {
        exceptionRule.expect(GoodReadsException.class);
        exceptionRule.expectMessage("Failed : HTTP Error code : " + HttpStatus.SC_INTERNAL_SERVER_ERROR);

        when(mockCloseableHttpClient.execute(mockHttpGet)).thenReturn(mockCloseableHttpResponse);
        when(mockCloseableHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        when(mockCloseableHttpResponse.getEntity()).thenReturn(mockHttpEntity);

        assertNotNull(bookReviewService.getXMLResponseFromGoodReadsAPI(QUERY_URL));
    }

    @Test
    public void getXMLResponseFromGoodReadsAPI_ErrorReadingResponse() throws IOException {

        exceptionRule.expect(GoodReadsException.class);
        exceptionRule.expectMessage("Error in reading Good Reads Response");

        when(mockCloseableHttpClient.execute(mockHttpGet)).thenReturn(mockCloseableHttpResponse);
        when(mockCloseableHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(mockCloseableHttpResponse.getEntity()).thenReturn(mockHttpEntity);

        assertNotNull(bookReviewService.getXMLResponseFromGoodReadsAPI("SAMPLE_URL"));
    }

    @Test
    public void testGetBooksListByAuthor() {
        bookList = new ArrayList<>();
        bookList.add(Book.builder().title("A").author("B").url("XYZ").build());
        bookList.add(Book.builder().title("B").author("A").url("ABC").build());
        bookList.sort(Comparator.comparing(Book::getAuthor));
        assertEquals(bookList, bookReviewService.getBooksListByAuthor(bookList));
    }

    @Test
    public void testGetBooksListByTitle() {
        bookList = new ArrayList<>();
        bookList.add(Book.builder().title("A").author("B").url("XYZ").build());
        bookList.add(Book.builder().title("B").author("A").url("ABC").build());
        bookList.sort(Comparator.comparing(Book::getTitle));
        assertEquals(bookList, bookReviewService.getBooksListByTitle(bookList));
    }

    @Test
    public void testGetPaginatedBooksList() {
        bookList = new ArrayList<>();
        Book book = Book.builder().title("A").author("B").url("XYZ").build();
        bookList.add(book);
        bookList.add(Book.builder().title("B").author("A").url("ABC").build());
        assertEquals(Arrays.asList(book),
                bookReviewService.getPaginatedBooksList(bookList, 0, 1));
    }

}
