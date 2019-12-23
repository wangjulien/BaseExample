package com.centreon.map.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.centreon.map.domain.enumeration.Status;

/**
 * A Service.
 */
@Entity
@Table(name = "service")
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JsonIgnoreProperties("services")
    private Host host;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Service serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Status getStatus() {
        return status;
    }

    public Service status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Host getHost() {
        return host;
    }

    public Service host(Host host) {
        this.host = host;
        return this;
    }

    public void setHost(Host host) {
        this.host = host;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Service)) {
            return false;
        }
        return id != null && id.equals(((Service) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Service{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
