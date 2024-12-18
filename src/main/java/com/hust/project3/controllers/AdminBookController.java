package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.book.BookRequestDTO;
import com.hust.project3.dtos.book.BookResponseDTO;
import com.hust.project3.entities.Book;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.BookService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/admin/books")
public class AdminBookController {
    private final BookService bookService;

    @GetMapping
    public Result getBooksByProperties(BookRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Page<BookResponseDTO> page = bookService.getVipBooksByProperties(dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getBookById(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(bookService.getVipBookById(id));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Result addBook(@RequestHeader("Authorization") String jwt,
                          @ModelAttribute @Valid BookRequestDTO dto) throws NotFoundException, IOException {
        return Result.ok(bookService.addBook(jwt, dto));
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Result updateBook(@RequestHeader("Authorization") String jwt,
                             @ModelAttribute @Valid BookRequestDTO dto) throws NotFoundException, MessagingException, IOException {
        return Result.ok(bookService.updateBook(jwt, dto));
    }

    @DeleteMapping
    public Result deleteBooks(@RequestHeader("Authorization") String jwt,
                             @RequestBody List<Long> idList) throws NotFoundException {
        return Result.ok(bookService.deleteBooks(jwt, idList));
    }
}
