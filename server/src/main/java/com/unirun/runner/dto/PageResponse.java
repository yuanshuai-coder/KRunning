package com.unirun.runner.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {

    private List<T> items;
    private int page;
    private int size;
    private long totalElements;
    private boolean hasNext;

    public PageResponse(List<T> items, int page, int size, long totalElements, boolean hasNext) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
    }

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.hasNext());
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
