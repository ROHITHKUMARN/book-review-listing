package com.dotdash.recruiting.bookreview.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.dotdash.recruiting.bookreview.client.constant.ApplicationConstants.*;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@Profile("client")
public class BookReviewApplicationClient implements ApplicationRunner {

    @Value("${application.url}")
    private String BASE_URL;

    public static void main(String[] args) {
        SpringApplication client = new SpringApplication(BookReviewApplicationClient.class);
        client.setAdditionalProfiles("client");
        client.run(args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption(HELP)) {
            System.out.println("Please use below options to get the desired results: \n" +
                    "--help to output a usage message and exit \n" +
                    "-s, --search to search the Goodreads' API and display the results on screen\n" +
                    "--sort to get sorted results where field is one of \"author\" or \"title\"\n" +
                    "-p a number to display the NUMBER page of results\n" +
                    "-h, --host to display the hostname or ip address where the server can be found");
            return;
        }
        if (args.containsOption(FULL_SEARCH)) {
            String keyWord = String.join("", args.getOptionValues(FULL_SEARCH));
            String url = BASE_URL += "books?search=" + keyWord;
            if (args.containsOption(SORT)) {
                String sortCriteria = String.join("", args.getOptionValues(SORT));
                if (sortCriteria.equalsIgnoreCase(AUTHOR)) {
                    url = url + "&sortByAuthor=true";
                } else if (sortCriteria.equalsIgnoreCase(TITLE)) {
                    url += "&sortByTitle=true";
                }
            }
            if (args.containsOption(PAGE) && args.containsOption(SIZE)) {
                String page = String.join("", args.getOptionValues(PAGE));
                String size = String.join("", args.getOptionValues(SIZE));
                url = url + "&page=" + page + "&size=" + size;
            }
            printResultSet(url);
            return;
        }

        if (args.containsOption(HOST) || args.containsOption(FULL_HOST)) {
            System.out.println("Application server is running on host= 127.0.0.1");
        }
    }

    private void printResultSet(String url) {
        List bookList = new RestTemplate().getForObject(url, List.class);
        if (bookList != null) {
            bookList.forEach(System.out::println);
        }
    }
}
