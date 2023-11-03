package fr.uga.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ehpad.
 */
@Entity
@Table(name = "ehpad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ehpad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_ehpad__user", joinColumns = @JoinColumn(name = "ehpad_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ehpad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "albumine", "ehpad", "users", "poids", "ePAS", "iMCS", "repas", "rappels", "notes" },
        allowSetters = true
    )
    private Set<Patient> patients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ehpad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Ehpad nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Ehpad users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Ehpad addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Ehpad removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        if (this.patients != null) {
            this.patients.forEach(i -> i.setEhpad(null));
        }
        if (patients != null) {
            patients.forEach(i -> i.setEhpad(this));
        }
        this.patients = patients;
    }

    public Ehpad patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public Ehpad addPatient(Patient patient) {
        this.patients.add(patient);
        patient.setEhpad(this);
        return this;
    }

    public Ehpad removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.setEhpad(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ehpad)) {
            return false;
        }
        return id != null && id.equals(((Ehpad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ehpad{" +
            "id=" + getId() +
            ", nom='" + getNom() + 
            ", users='" + getUsers() + "'" +
            "}";
    }
}
