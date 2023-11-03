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
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "statut")
    private String statut;

    @Column(name = "date_naissance")
    private Instant dateNaissance;

    @Column(name = "taille")
    private Float taille;

    @Column(name = "datearrive")
    private Instant datearrive;

    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Albumine albumine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "users", "patients" }, allowSetters = true)
    private Ehpad ehpad;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_patient__user",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Poids> poids = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<EPA> ePAS = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<IMC> iMCS = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "aliments" }, allowSetters = true)
    private Set<Repas> repas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "patient" }, allowSetters = true)
    private Set<Rappel> rappels = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "patient" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Patient nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Patient prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getStatut() {
        return this.statut;
    }

    public Patient statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public Patient dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Float getTaille() {
        return this.taille;
    }

    public Patient taille(Float taille) {
        this.setTaille(taille);
        return this;
    }

    public void setTaille(Float taille) {
        this.taille = taille;
    }

    public Instant getDatearrive() {
        return this.datearrive;
    }

    public Patient datearrive(Instant datearrive) {
        this.setDatearrive(datearrive);
        return this;
    }

    public void setDatearrive(Instant datearrive) {
        this.datearrive = datearrive;
    }

    public Albumine getAlbumine() {
        return this.albumine;
    }

    public void setAlbumine(Albumine albumine) {
        this.albumine = albumine;
    }

    public Patient albumine(Albumine albumine) {
        this.setAlbumine(albumine);
        return this;
    }

    public Ehpad getEhpad() {
        return this.ehpad;
    }

    public void setEhpad(Ehpad ehpad) {
        this.ehpad = ehpad;
    }

    public Patient ehpad(Ehpad ehpad) {
        this.setEhpad(ehpad);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Patient users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Patient addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Patient removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Set<Poids> getPoids() {
        return this.poids;
    }

    public void setPoids(Set<Poids> poids) {
        if (this.poids != null) {
            this.poids.forEach(i -> i.setPatient(null));
        }
        if (poids != null) {
            poids.forEach(i -> i.setPatient(this));
        }
        this.poids = poids;
    }

    public Patient poids(Set<Poids> poids) {
        this.setPoids(poids);
        return this;
    }

    public Patient addPoids(Poids poids) {
        this.poids.add(poids);
        poids.setPatient(this);
        return this;
    }

    public Patient removePoids(Poids poids) {
        this.poids.remove(poids);
        poids.setPatient(null);
        return this;
    }

    public Set<EPA> getEPAS() {
        return this.ePAS;
    }

    public void setEPAS(Set<EPA> ePAS) {
        if (this.ePAS != null) {
            this.ePAS.forEach(i -> i.setPatient(null));
        }
        if (ePAS != null) {
            ePAS.forEach(i -> i.setPatient(this));
        }
        this.ePAS = ePAS;
    }

    public Patient ePAS(Set<EPA> ePAS) {
        this.setEPAS(ePAS);
        return this;
    }

    public Patient addEPA(EPA ePA) {
        this.ePAS.add(ePA);
        ePA.setPatient(this);
        return this;
    }

    public Patient removeEPA(EPA ePA) {
        this.ePAS.remove(ePA);
        ePA.setPatient(null);
        return this;
    }

    public Set<IMC> getIMCS() {
        return this.iMCS;
    }

    public void setIMCS(Set<IMC> iMCS) {
        if (this.iMCS != null) {
            this.iMCS.forEach(i -> i.setPatient(null));
        }
        if (iMCS != null) {
            iMCS.forEach(i -> i.setPatient(this));
        }
        this.iMCS = iMCS;
    }

    public Patient iMCS(Set<IMC> iMCS) {
        this.setIMCS(iMCS);
        return this;
    }

    public Patient addIMC(IMC iMC) {
        this.iMCS.add(iMC);
        iMC.setPatient(this);
        return this;
    }

    public Patient removeIMC(IMC iMC) {
        this.iMCS.remove(iMC);
        iMC.setPatient(null);
        return this;
    }

    public Set<Repas> getRepas() {
        return this.repas;
    }

    public void setRepas(Set<Repas> repas) {
        if (this.repas != null) {
            this.repas.forEach(i -> i.setPatient(null));
        }
        if (repas != null) {
            repas.forEach(i -> i.setPatient(this));
        }
        this.repas = repas;
    }

    public Patient repas(Set<Repas> repas) {
        this.setRepas(repas);
        return this;
    }

    public Patient addRepas(Repas repas) {
        this.repas.add(repas);
        repas.setPatient(this);
        return this;
    }

    public Patient removeRepas(Repas repas) {
        this.repas.remove(repas);
        repas.setPatient(null);
        return this;
    }

    public Set<Rappel> getRappels() {
        return this.rappels;
    }

    public void setRappels(Set<Rappel> rappels) {
        if (this.rappels != null) {
            this.rappels.forEach(i -> i.setPatient(null));
        }
        if (rappels != null) {
            rappels.forEach(i -> i.setPatient(this));
        }
        this.rappels = rappels;
    }

    public Patient rappels(Set<Rappel> rappels) {
        this.setRappels(rappels);
        return this;
    }

    public Patient addRappel(Rappel rappel) {
        this.rappels.add(rappel);
        rappel.setPatient(this);
        return this;
    }

    public Patient removeRappel(Rappel rappel) {
        this.rappels.remove(rappel);
        rappel.setPatient(null);
        return this;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setPatient(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setPatient(this));
        }
        this.notes = notes;
    }

    public Patient notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Patient addNote(Note note) {
        this.notes.add(note);
        note.setPatient(this);
        return this;
    }

    public Patient removeNote(Note note) {
        this.notes.remove(note);
        note.setPatient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", taille=" + getTaille() +
            ", datearrive='" + getDatearrive() + 
            ", albumine='" + getAlbumine() + "'" +
            ", ehpad='" + getEhpad() + "'" +
            ", users='" + getUsers() + "'" +
            "}";
    }
}
