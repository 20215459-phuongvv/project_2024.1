package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.borrowing.BorrowBookRequestDTO;
import com.hust.project3.entities.BorrowBook;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.BorrowBookService;
import com.hust.project3.services.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrow")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;
    private final SettingService settingService;

    @GetMapping
    public Result getBorrowingsByUserLogin(@RequestHeader("Authorization") String jwt,
                                           BorrowBookRequestDTO dto,
                                           PagingRequestDTO pagingRequestDTO) throws NotFoundException {
        Page<BorrowBook> page = borrowBookService.getBorrowingsByUserLogin(jwt, dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/limits")
    public Result getBorrowBookLimit(@RequestHeader("Authorization") String jwt) throws NotFoundException {
        return Result.ok(settingService.getUserSettings(jwt));
    }

    @GetMapping("/{id}")
    public Result getBorrowingById(@RequestHeader("Authorization") String jwt,
                                   @PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(borrowBookService.getUserBorrowingById(jwt, id));
    }

    @PostMapping
    public Result addBorrowBook(@RequestHeader("Authorization") String jwt,
                                @RequestBody BorrowBookRequestDTO dto) throws NotFoundException, BadRequestException {
        return Result.ok(borrowBookService.addBorrowBook(jwt, dto));
    }
}
