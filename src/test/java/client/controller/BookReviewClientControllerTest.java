package client.controller;

import com.dotdash.recruiting.bookreview.client.BookReviewApplicationClient;
import com.dotdash.recruiting.bookreview.client.constant.ApplicationConstants;
import com.dotdash.recruiting.bookreview.client.controller.BookReviewClientController;
import com.dotdash.recruiting.bookreview.server.model.Book;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationConstants.class,
        BookReviewClientController.class, BookReviewApplicationClient.class})
@WebMvcTest(BookReviewClientController.class)
public class BookReviewClientControllerTest {

    @Autowired
    BookReviewClientController bookReviewClientController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private static final String baseUrl = "http://localhost:8080/api/";
    private static final String serverUrl = "http://localhost:8081/";

    @Before
    public void setup() {
        ReflectionTestUtils.setField(bookReviewClientController, "BASE_URL", baseUrl);
    }

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getBooks_NoBooks() throws Exception {
        List<Book> bookList = Lists.newArrayList(
                Book.builder().author("X").title("B").url("http://x.png").build(),
                Book.builder().author("Y").title("A").url("http://y.png").build()
        );

        when(restTemplate.getForObject(baseUrl + "?search=example", List.class))
                .thenReturn(bookList);

        this.mockMvc.perform(get(serverUrl + "?search=example"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE));
    }

}
