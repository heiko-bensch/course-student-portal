package de.bensch.course.controller;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

class PageUtilsTest {

    @Test
    void addPaginationAttributesToModel_ForEmptyPage() {
        Model model = new ExtendedModelMap();
        Page<Integer> page = new PageImpl<>(Collections.EMPTY_LIST, Pageable.unpaged(), 0L);
        PageUtils.addPaginationAttributesToModel(model, page);

        assertThat(model.getAttribute("currentPage")).isEqualTo(1);
        assertThat(model.getAttribute("totalItems")).isEqualTo(0L);
        assertThat(model.getAttribute("totalPages")).isEqualTo(1);
        assertThat(model.getAttribute("pageSize")).isEqualTo(0);
        assertThat(model.getAttribute("sortField")).isNull();
        assertThat(model.getAttribute("sortDirection")).isNull();
        assertThat(model.getAttribute("reverseSortDirection")).isNull();
    }

    @Test
    void addPaginationAttributesToModel_forNonEmptyPages_withSorting() {
        Model model = new ExtendedModelMap();
        List<Integer> list = IntStream.range(0, 100).boxed()
                .toList();
        Pageable pageable = PageRequest.of(2, 15, Sort.by("id"));
        Page<Integer> page = new PageImpl<>(list, pageable, list.size());

        PageUtils.addPaginationAttributesToModel(model, page);

        assertThat(model.getAttribute("currentPage")).isEqualTo(3);
        assertThat(model.getAttribute("totalItems")).isEqualTo((long) list.size());
        assertThat(model.getAttribute("totalPages")).isEqualTo(7);
        assertThat(model.getAttribute("pageSize")).isEqualTo(15);

        assertThat(model.getAttribute("sortField")).isEqualTo("id");
        assertThat(model.getAttribute("sortDirection")).isEqualTo("asc");
        assertThat(model.getAttribute("reverseSortDirection")).isEqualTo("desc");
    }

    @Test
    void createPageable() {
        Pageable pageable = PageUtils.createPageable(1, 10, new String[]{ "id", "asc" });
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().isSorted()).isTrue();
        assertThat(pageable.getSort().getOrderFor("id").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void createPageable_withDefaultSort() {
        Pageable pageable = PageUtils.createPageable(1, 10);
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().isSorted()).isFalse();
    }
}
