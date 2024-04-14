package com.quizapp.worklingo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageDTO <T> {
    private long totalPages;
    private long totalElements;
    private Integer currentPage;
    private Integer pageSize;
    private List<T> results;

    public PageDTO(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.results = page.getContent();
    }
}
