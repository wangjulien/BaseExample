package com.centreon.map.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.centreon.map.domain.enumeration.Status;

/**
 * A Host.
 */
@Entity
@Table(name = "host")
public class Host implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "host_name", nullable = false)
    private String hostName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "host")
    private Set<Service> services = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public Host hostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Status getStatus() {
        return status;
    }

    public Host status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Service> getServices() {
        return services;
    }

    public Host services(Set<Service> services) {
        this.services = services;
        return this;
    }

    public Host addService(Service service) {
        this.services.add(service);
        service.setHost(this);
        return this;
    }

    public Host removeService(Service service) {
        this.services.remove(service);
        service.setHost(null);
        return this;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Host)) {
            return false;
        }
        return id != null && id.equals(((Host) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Host{" +
            "id=" + getId() +
            ", hostName='" + getHostName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
