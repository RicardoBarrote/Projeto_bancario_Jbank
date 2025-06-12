package com.fbr.jbank.controller.dto;

import org.springframework.data.domain.Page;

public record PaginationDto(Integer page,
                            Integer pageSize,
                            Long totalElements,
                            Integer totalPages) {

    public static <T> PaginationDto convertToDto(Page<T> page) {
        return new PaginationDto(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
