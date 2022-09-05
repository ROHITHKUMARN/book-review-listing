package com.dotdash.recruiting.bookreview.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Controller
public class BookReviewClientController {

    @Value("${application.url}")
    private String BASE_URL;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * @param model        : The model to render the data to UI
     * @param search       : The non-{@code null},empty and blank search keyword
     * @param sortByAuthor : An {@link Optional}  param to sort the Book list by Author name
     * @param sortByTitle: An {@link Optional} param to sort the book list by Title name
     * @return : The name of the thymeleaf template to render the UI
     */
    @GetMapping("/showBooks")
    public String showBooksList(Model model,
                                @RequestParam String search,
                                @RequestParam(value = "sortByAuthor", required = false) Optional<Boolean> sortByAuthor,
                                @RequestParam(value = "sortByTitle", required = false) Optional<Boolean> sortByTitle,
                                @RequestParam(value = "page", required = false) Optional<Integer> page,
                                @RequestParam(value = "size", required = false) Optional<Integer> size) {
        if (null == search || search.isBlank()) {
            return "error";
        }
        List bookList;
        String url = BASE_URL + "books?search=" + search;
        if (sortByAuthor.isPresent() && sortByAuthor.get()) {
            url = url + "&sortByAuthor=true";
        } else if (sortByTitle.isPresent() && sortByTitle.get()) {
            url = url + "&sortByTitle=true";
        }
        if (page.isPresent() && size.isPresent()) {
            url = url + "&page=" + page.get() + "&size=" + size.get();
        }
        bookList = restTemplate.getForObject(url, List.class);
        model.addAttribute("books", bookList);
        return "index";
    }
}
