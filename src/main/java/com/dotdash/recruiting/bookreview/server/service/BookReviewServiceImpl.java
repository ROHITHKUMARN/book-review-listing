package com.dotdash.recruiting.bookreview.server.service;

import com.dotdash.recruiting.bookreview.server.exception.GoodReadsException;
import com.dotdash.recruiting.bookreview.server.model.Book;
import com.dotdash.recruiting.bookreview.server.utils.HttpClientFactory;
import com.dotdash.recruiting.bookreview.server.utils.HttpClientFactoryImpl;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BookReviewServiceImpl implements BookReviewService {

    private static final Logger logger = LoggerFactory.getLogger(BookReviewServiceImpl.class);

    @Value("${goodreads.api.key}")
    private String goodReadsKey;

    @Value("${goodreads.api.url}")
    private String goodReadsURL;

    private final HttpClientFactory httpClientFactory;

    public BookReviewServiceImpl() {
        this.httpClientFactory = new HttpClientFactoryImpl();
    }

    @Override
    public List<Book> getBooksList(final String keyword) {
        String requestUrl = getGoodReadsUrl(keyword);
        String XMLResponse = getXMLResponseFromGoodReadsAPI(requestUrl);
        JSONObject JSONResponse = getJsonResponseFromXML(XMLResponse);
        List<Book> bookList = getBooksFromJsonResponse(JSONResponse);
        return getBooksListByTitle(bookList);
    }

    @Override
    public List<Book> getBooksListByTitle(List<Book> bookList) {
        bookList.sort(Comparator.comparing(Book::getTitle));
        return bookList;
    }

    @Override
    public List<Book> getBooksListByAuthor(List<Book> bookList) {
        bookList.sort(Comparator.comparing(Book::getAuthor));
        return bookList;
    }

    @Override
    public List<Book> getPaginatedBooksList(List<Book> bookList, int page, int size) {
        int totalNumberOfBooks = bookList.size();
        List<List<Book>> pagedBookList = new ArrayList<>();
        for (int i = 0; i < totalNumberOfBooks; ) {
            List<Book> pagedBook = new ArrayList<>();
            int j = 0;
            while (j < size && (i + j < totalNumberOfBooks)) {
                pagedBook.add(bookList.get(i + j));
                j++;
            }
            pagedBookList.add(pagedBook);
            i = i + j;
        }
        return pagedBookList.get(page);
    }

    /**
     * Returns the XML response from Good Reads API
     *
     * @param requestUrl The non-{@code null}, non-empty, and non-blank url
     * @return The XML Response from the Good Reads Url
     */
    public String getXMLResponseFromGoodReadsAPI(String requestUrl) {
        String responseString;
        if (StringUtils.isBlank(requestUrl)) {
            throw new IllegalArgumentException("Invalid Good Reads URL");
        }
        try {
            HttpUriRequest request = new HttpGet(requestUrl);
            CloseableHttpClient closeableHttpClient = httpClientFactory.createCloseableHttpClient();
            CloseableHttpResponse httpResponse = closeableHttpClient.execute(request);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("Problem in fetching XML Response from Good Reads API");
                throw new GoodReadsException("Failed : HTTP Error code : "
                        + httpResponse.getStatusLine().getStatusCode());
            }
            HttpEntity entity = httpResponse.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            logger.error("Problem in Reading XML Response from GoodReads API {}", e.getMessage());
            throw new GoodReadsException("Error in reading Good Reads Response");
        }
        return responseString;
    }

    /**
     * Returns Good Reads API Url
     *
     * @param keyword The non-{@code null}, non-empty and non-blank keyword to search the API
     * @return The Good Read's API Url
     */
    private String getGoodReadsUrl(String keyword) {
        return goodReadsURL + "?key=" + goodReadsKey + "&q=" + keyword;
    }

    /**
     * Returns {@link JSONObject} Response
     *
     * @param xmlResponse The non-{@code null} xmlResponse from Good Reads API
     * @return The {@link JSONObject} response from XML
     */
    private JSONObject getJsonResponseFromXML(String xmlResponse) {
        JSONObject json;
        try {
            json = XML.toJSONObject(xmlResponse);
        } catch (JSONException e) {
            logger.error("Problem in converting XML to JSON {}", e.getMessage());
            throw new GoodReadsException("Error in converting XML to JSON");
        }
        return json;
    }


    /**
     * Returns {@link List} of {@link Book} from the JSON Response
     *
     * @param jsonResponse The non-{@code null} json response to process
     * @return The {@link List} of {@link Book}
     */
    private List<Book> getBooksFromJsonResponse(JSONObject jsonResponse) {
        List<Book> bookList = new ArrayList<>();
        JSONArray workList = jsonResponse.getJSONObject("GoodreadsResponse")
                .getJSONObject("search")
                .getJSONObject("results")
                .getJSONArray("work");
        for (int i = 0; i < workList.length(); i++) {
            JSONObject work = (JSONObject) workList.get(i);
            JSONObject best_book = work.getJSONObject("best_book");
            JSONObject author = best_book.getJSONObject("author");
            Book book = Book.builder()
                    .author(author.getString("name"))
                    .title(best_book.getString("title"))
                    .url(best_book.getString("image_url"))
                    .build();
            bookList.add(book);
        }
        return bookList;
    }

}
