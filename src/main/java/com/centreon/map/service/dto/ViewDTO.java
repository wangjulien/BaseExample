package com.centreon.map.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.centreon.map.domain.View} entity.
 */
public class ViewDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Boolean isLocked;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
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

        ViewDTO viewDTO = (ViewDTO) o;
        if (viewDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), viewDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ViewDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", isLocked='" + isIsLocked() + "'" +
            "}";
    }
}
