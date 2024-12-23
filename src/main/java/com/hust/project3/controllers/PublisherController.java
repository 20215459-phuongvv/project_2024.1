package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.publisher.PublisherRequestDTO;
import com.hust.project3.entities.Publisher;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping
    public Result getPublishersByProperties(PublisherRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Page<Publisher> page = publisherService.getPublishersByProperties(dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getPublisherById(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(publisherService.getPublisherById(id));
    }
}
