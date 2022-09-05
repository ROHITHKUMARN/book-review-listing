package client;

import com.dotdash.recruiting.bookreview.client.BookReviewApplicationClient;
import com.dotdash.recruiting.bookreview.client.controller.BookReviewClientController;
import com.dotdash.recruiting.bookreview.server.BookReviewApplicationServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BookReviewApplicationClient.class})
@WebMvcTest(BookReviewClientController.class)
@Profile("test")
public class BookReviewApplicationClientTest {

    @MockBean
    BookReviewClientController bookReviewClientController;

    @Test
    public void contextLoads() {
        assertThat(bookReviewClientController).isNotNull();
    }

    @Test
    public void main() {
        String[] arguments = {"--help"};
        BookReviewApplicationClient.main(arguments);
    }
}
