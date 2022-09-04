package com.dotdash.recruiting.bookreview.server.service;

import com.dotdash.recruiting.bookreview.server.model.Book;

import java.util.List;

public interface BookReviewService {

    /**
     * Gets a {@link List} of {@link Book}s from Good Reads API given the search keyword
     *
     * @param keyword The non-{@code null}, non-empty and non-blank search keyword
     * @return The {@link List} of {@link Book}s
     */
    List<Book> getBooksList(final String keyword);

    /**
     * Gets sorted {@link List} of {@link Book}s from Good Reads API sorted by Author name
     *
     * @param bookList The non-{@code null} list of books
     * @return The sorted {@link List} of {@link Book}s
     */
    List<Book> getBooksListByTitle(List<Book> bookList);

    /**
     * Gets sorted {@link List} of {@link Book}s from Good Reads API sorted by Title name
     *
     * @param bookList The non-{@code null} list of books
     * @return The sorted {@link List} of {@link Book}s
     */
    List<Book> getBooksListByAuthor(List<Book> bookList);

    /**
     * Gets sorted {@link List} of {@link Book}s from Good Reads API sorted by Title name
     *
     * @param bookList The non-{@code null list of books
     * @param page     The page number
     * @param size     The page size
     * @return
     */
    List<Book> getPaginatedBooksList(List<Book> bookList, int page, int size);
}
