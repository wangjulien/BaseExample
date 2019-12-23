package com.centreon.map.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.centreon.map.domain.View} entity. This class is used
 * in {@link com.centreon.map.web.rest.ViewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /views?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ViewCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private BooleanFilter isLocked;

    public ViewCriteria(){
    }

    public ViewCriteria(ViewCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.isLocked = other.isLocked == null ? null : other.isLocked.copy();
    }

    @Override
    public ViewCriteria copy() {
        return new ViewCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public BooleanFilter getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(BooleanFilter isLocked) {
        this.isLocked = isLocked;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ViewCriteria that = (ViewCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(isLocked, that.isLocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        isLocked
        );
    }

    @Override
    public String toString() {
        return "ViewCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (isLocked != null ? "isLocked=" + isLocked + ", " : "") +
            "}";
    }

}
