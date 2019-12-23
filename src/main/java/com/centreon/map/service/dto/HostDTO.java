package com.centreon.map.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.centreon.map.domain.enumeration.Status;

/**
 * A DTO for the {@link com.centreon.map.domain.Host} entity.
 */
public class HostDTO implements Serializable {

    private Long id;

    @NotNull
    private String hostName;

    private Status status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HostDTO hostDTO = (HostDTO) o;
        if (hostDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hostDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HostDTO{" +
            "id=" + getId() +
            ", hostName='" + getHostName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
