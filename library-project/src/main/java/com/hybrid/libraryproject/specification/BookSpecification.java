package com.hybrid.libraryproject.specification;

import com.hybrid.libraryproject.model.Author;
import com.hybrid.libraryproject.model.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookSpecification implements Specification<Book> {

    public Map<String, String> queryParams;

    public BookSpecification(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (queryParams.containsKey("id")) {
            String id = queryParams.get("id");
            predicates.add(criteriaBuilder.equal(root.get("id"), Long.parseLong(id)));
        }

        if (queryParams.containsKey("title")) {
            String title = queryParams.get("title");
            predicates.add(criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (queryParams.containsKey("creationDate")) {
            String creationDate = queryParams.get("creationDate");
            predicates.add(criteriaBuilder.equal(root.get("creationDate"), LocalDate.parse(creationDate)));
        }

        if (queryParams.containsKey("isbn")) {
            String isbn = queryParams.get("isbn");
            predicates.add(criteriaBuilder.equal(root.get("isbn"), isbn));
        }

        if (queryParams.containsKey("author")) {
            String authorName = queryParams.get("author");
            SetJoin<Book, Author> joinAuthors = root.joinSet("authors", JoinType.INNER);
            predicates.add(criteriaBuilder.like(joinAuthors.get("name"), "%" + authorName + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
