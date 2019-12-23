package com.centreon.map.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A View.
 */
@Entity
@Table(name = "view")
public class View implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @NotNull
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public View title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isIsLocked() {
        return isLocked;
    }

    public View isLocked(Boolean isLocked) {
        this.isLocked = isLocked;
        return this;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof View)) {
            return false;
        }
        return id != null && id.equals(((View) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "View{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", isLocked='" + isIsLocked() + "'" +
            "}";
    }
}
