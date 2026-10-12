package com.example.model.singletable;

import javax.persistence.*;

/**
 * Classe mère de la hiérarchie des véhicules — stratégie <b>SINGLE_TABLE</b>.
 *
 * <p>Toutes les sous-classes ({@link Voiture}, {@link com.example.model.singletable.Developpeur}) sont rangées dans
 * <b>une seule table</b> {@code Vehicule}. La colonne {@code vehicule_type}
 * (le discriminateur) indique le type réel de chaque ligne.
 *
 * <p>La classe est {@code abstract} : on ne crée jamais un "véhicule" tout court,
 * seulement une voiture ou une moto.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicule_type", discriminatorType = DiscriminatorType.STRING)
// Bonne pratique de l'énoncé : indexer la colonne discriminante
@Table(indexes = @Index(name = "idx_vehicule_type", columnList = "vehicule_type"))
public abstract class Vehicule {

    /** Clé primaire, générée par la base (auto-incrément). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Marque du véhicule, obligatoire. */
    @Column(nullable = false)
    private String marque;

    /** Constructeur vide exigé par JPA. */
    public Vehicule() {}

    /** @param marque marque du véhicule */
    public Vehicule(String marque) {
        this.marque = marque;
    }

    public Long getId() { return id; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
}