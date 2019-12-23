package com.centreon.map.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.centreon.map.domain.enumeration.Status;

/**
 * A DTO for the {@link com.centreon.map.domain.Service} entity.
 */
public class ServiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String serviceName;

    private Status status;


    private Long hostId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceDTO serviceDTO = (ServiceDTO) o;
        if (serviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceDTO{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", status='" + getStatus() + "'" +
            ", host=" + getHostId() +
            "}";
    }
}
