package fr.uga.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Aliment.
 */
@Entity
@Table(name = "aliment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Aliment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "calories")
    private Float calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "patient", "aliments" }, allowSetters = true)
    private Repas repas;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Aliment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Aliment nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Float getCalories() {
        return this.calories;
    }

    public Aliment calories(Float calories) {
        this.setCalories(calories);
        return this;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public Repas getRepas() {
        return this.repas;
    }

    public void setRepas(Repas repas) {
        this.repas = repas;
    }

    public Aliment repas(Repas repas) {
        this.setRepas(repas);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aliment)) {
            return false;
        }
        return id != null && id.equals(((Aliment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aliment{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", calories=" + getCalories() +
            ", repas=" + getRepas() + 
            "}";
    }
}
