package com.dotdash.recruiting.bookreview.server.controller;

import com.dotdash.recruiting.bookreview.server.service.BookReviewService;
import com.dotdash.recruiting.bookreview.server.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BookReviewServerController {

    @Autowired
    BookReviewService bookService;

    /**
     * Rest End Point to return {@link List} of {@link Book} based on search keyword
     *
     * @param search       : The non-{@code null}, non-empty, non-blank search keyword
     * @param sortByAuthor :  An {@link Optional}  param to sort the Book list by Author name
     * @param sortByTitle  :An {@link Optional} param to sort the book list by Title name
     * @return : The {@link List} of {@link Book}
     */
    @GetMapping("/api/books")
    public ResponseEntity<List<Book>> getBookList(@RequestParam String search,
                                                  @RequestParam(value = "sortByAuthor", required = false) Optional<Boolean> sortByAuthor,
                                                  @RequestParam(value = "sortByTitle", required = false) Optional<Boolean> sortByTitle,
                                                  @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                  @RequestParam(value = "size", required = false) Optional<Integer> size) {
        List<Book> bookList = bookService.getBooksList(search);

        if (sortByAuthor.isPresent() && sortByAuthor.get()) {
            bookList = bookService.getBooksListByAuthor(bookList);
        }
        if (sortByTitle.isPresent() && sortByTitle.get()) {
            bookList = bookService.getBooksListByTitle(bookList);
        }
        if (page.isPresent() && size.isPresent()) {
            int pageNumber = page.get();
            int pageSize = size.get();
            bookList = bookService.getPaginatedBooksList(bookList, pageNumber, pageSize);
        }
        if(null == bookList){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

}