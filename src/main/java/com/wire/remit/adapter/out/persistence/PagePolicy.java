package com.wire.remit.adapter.out.persistence;

import org.springframework.stereotype.Component;

@Component
public class PagePolicy {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    public long offset(Integer page, Integer size) {
        return (long) (getPage(page) - 1) * getSize(size);
    }

    public int getPage(Integer page) {
        return (page == null || page <= 0) ? DEFAULT_PAGE : page;
    }

    public int getSize(Integer size) {
        if (size == null || size <= 0) return DEFAULT_SIZE;
        return Math.min(size, MAX_SIZE);
    }
}
