package com.hust.project3.dtos;

import org.springframework.data.domain.Page;

public record ResultMeta(int page, int size, long total) {
    public static <T> ResultMeta of(Page<T> page) {
        return new ResultMeta(page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
