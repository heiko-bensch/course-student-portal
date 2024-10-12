package de.bensch.course.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

public class Utils {
    private Utils() {
    }

    public static Pageable createPageable(int page, int size, String[] sort) {
        Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sort[0]);
        return PageRequest.of(page - 1, size, Sort.by(order));
    }

    public static Pageable createPageable(int page, int size) {
        return PageRequest.of(page - 1, size, Sort.unsorted());
    }

    public static void addPaginationAttributesToModel(Model model, Page<?> page) {
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageSize", page.getSize());
        if (page.getSort().isSorted()) {
            Sort.Order order = page.getSort().stream().findFirst().orElse(new Sort.Order(Sort.Direction.ASC, "id"));
            model.addAttribute("sortField", order.getProperty());
            model.addAttribute("sortDirection", (order.isAscending()) ? "asc" : "desc");
            model.addAttribute("reverseSortDirection", order.isAscending() ? "desc" : "asc");
        }
    }
}
