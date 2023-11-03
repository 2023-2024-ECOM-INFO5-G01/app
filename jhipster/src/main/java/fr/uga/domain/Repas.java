package fr.uga.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Repas.
 */
@Entity
@Table(name = "repas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Repas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @Column(name = "calories")
    private Float calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "albumine", "ehpad", "users", "poids", "ePAS", "iMCS", "repas", "rappels", "notes" },
        allowSetters = true
    )
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "repas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "repas" }, allowSetters = true)
    private Set<Aliment> aliments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Repas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Repas date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Float getCalories() {
        return this.calories;
    }

    public Repas calories(Float calories) {
        this.setCalories(calories);
        return this;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Repas patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public Set<Aliment> getAliments() {
        return this.aliments;
    }

    public void setAliments(Set<Aliment> aliments) {
        if (this.aliments != null) {
            this.aliments.forEach(i -> i.setRepas(null));
        }
        if (aliments != null) {
            aliments.forEach(i -> i.setRepas(this));
        }
        this.aliments = aliments;
    }

    public Repas aliments(Set<Aliment> aliments) {
        this.setAliments(aliments);
        return this;
    }

    public Repas addAliment(Aliment aliment) {
        this.aliments.add(aliment);
        aliment.setRepas(this);
        return this;
    }

    public Repas removeAliment(Aliment aliment) {
        this.aliments.remove(aliment);
        aliment.setRepas(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Repas)) {
            return false;
        }
        return id != null && id.equals(((Repas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Repas{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", calories=" + getCalories() +
            ", patient='" + getPatient() + "'" +
            "}";
    }
}
