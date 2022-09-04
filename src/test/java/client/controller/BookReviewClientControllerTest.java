package client.controller;

import com.dotdash.recruiting.bookreview.client.controller.BookReviewClientController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookReviewClientController.class)
@WebAppConfiguration
public class BookReviewClientControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getProductsList() throws Exception {
        String uri = "http://localhost:8080/showBooks?search=gandhi";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.ALL))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
        assertTrue(content.contains("index"));
        assertEquals(200, status);
    }

}
