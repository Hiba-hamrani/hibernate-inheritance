package com.example.model.joined;

import javax.persistence.*;

/**
 * Classe mère de la hiérarchie des employés — stratégie <b>JOINED</b>.
 *
 * <p>Chaque classe a <b>sa propre table</b> :
 * <ul>
 *   <li>{@code Employe} contient les colonnes communes ({@code id}, {@code nom}) ;</li>
 *   <li>{@code Developpeur} et {@code Manager} contiennent seulement leurs colonnes
 *       propres, et leur {@code id} est une clé étrangère vers {@code Employe.id}.</li>
 * </ul>
 * Pour relire un employé, Hibernate fait donc une <b>jointure</b> entre ces tables.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employe {

    /** Clé primaire, générée par la base, partagée avec la table de la sous-classe. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de l'employé, obligatoire. */
    @Column(nullable = false)
    private String nom;

    /** Constructeur vide exigé par JPA. */
    public Employe() {}

    /** @param nom nom de l'employé */
    public Employe(String nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}