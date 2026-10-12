package com.example;

import com.example.model.joined.Developpeur;
import com.example.model.joined.Employe;
import com.example.model.joined.Manager;
import com.example.model.singletable.Moto;
import com.example.model.singletable.Vehicule;
import com.example.model.singletable.Voiture;
import com.example.model.tableperclass.Electronique;
import com.example.model.tableperclass.Livre;
import com.example.model.tableperclass.Produit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests des trois stratégies d'héritage.
 * Une seule EntityManagerFactory pour toute la classe, un EntityManager neuf par test,
 * et la base vidée après chaque test pour que les tests restent indépendants.
 */
public class HeritageTest {

    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeClass
    public static void creerUsine() {
        emf = Persistence.createEntityManagerFactory("hibernate-inheritance");
    }

    @AfterClass
    public static void fermerUsine() {
        if (emf != null) emf.close();
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
        em.clear();
        em.getTransaction().begin();
        em.createQuery("SELECT v FROM Vehicule v", Vehicule.class).getResultList().forEach(em::remove);
        em.createQuery("SELECT e FROM Employe e", Employe.class).getResultList().forEach(em::remove);
        em.createQuery("SELECT p FROM Produit p", Produit.class).getResultList().forEach(em::remove);
        em.getTransaction().commit();
        em.close();
    }

    // ---------- Outils ----------

    private void enregistrer(Object... entites) {
        em.getTransaction().begin();
        for (Object e : entites) em.persist(e);
        em.getTransaction().commit();
        em.clear(); // les lectures suivantes vont vraiment en base
    }

    /** Nombre de lignes d'une table, en SQL natif (pour vérifier le schéma réel). */
    private long lignesTable(String table) {
        return ((Number) em.createNativeQuery("SELECT COUNT(*) FROM " + table).getSingleResult()).longValue();
    }

    /** Vrai si la table existe dans le schéma H2. */
    private boolean tableExiste(String table) {
        Number n = (Number) em.createNativeQuery(
                        "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = ?")
                .setParameter(1, table.toUpperCase())
                .getSingleResult();
        return n.longValue() > 0;
    }

    // ---------- SINGLE_TABLE ----------

    @Test
    public void singleTable_requetePolymorphiqueRenvoieLesBonsTypes() {
        enregistrer(new Voiture("Toyota", 4), new Moto("Harley", true));

        List<Vehicule> vehicules = em.createQuery("SELECT v FROM Vehicule v ORDER BY v.marque", Vehicule.class)
                .getResultList();

        assertEquals(2, vehicules.size());
        assertTrue(vehicules.get(0) instanceof Moto);     // Harley
        assertTrue(vehicules.get(1) instanceof Voiture);  // Toyota
    }

    @Test
    public void singleTable_uneSeuleTableAvecDiscriminateur() {
        enregistrer(new Voiture("Toyota", 4), new Moto("Harley", true));

        assertEquals("Les 2 véhicules sont dans la même table", 2, lignesTable("Vehicule"));
        assertFalse("Pas de table séparée pour Voiture", tableExiste("Voiture"));

        @SuppressWarnings("unchecked")
        List<String> types = em.createNativeQuery("SELECT vehicule_type FROM Vehicule ORDER BY vehicule_type")
                .getResultList();
        assertEquals(List.of("MOTO", "VOITURE"), types);
    }

    @Test
    public void singleTable_requeteSurUneSousClasseFiltreParLeDiscriminateur() {
        enregistrer(new Voiture("Toyota", 4), new Voiture("Renault", 5), new Moto("Harley", true));

        List<Voiture> voitures = em.createQuery("SELECT v FROM Voiture v", Voiture.class).getResultList();

        assertEquals(2, voitures.size());
    }

    // ---------- JOINED ----------

    @Test
    public void joined_uneTableParClasseLieesParLId() {
        Developpeur dev = new Developpeur("Alice", "Java");
        enregistrer(dev, new Manager("Bob", 3));

        assertEquals("Table mère : tous les employés", 2, lignesTable("Employe"));
        assertEquals("Table fille : seulement les développeurs", 1, lignesTable("Developpeur"));
        assertEquals(1, lignesTable("Manager"));

        Employe relu = em.find(Employe.class, dev.getId());
        assertTrue(relu instanceof Developpeur);
        assertEquals("Java", ((Developpeur) relu).getLangage());
    }

    @Test
    public void joined_modificationDUneColonneDeLaSousClasse() {
        Developpeur dev = new Developpeur("Alice", "Java");
        enregistrer(dev);

        em.getTransaction().begin();
        em.find(Developpeur.class, dev.getId()).setLangage("Kotlin");
        em.getTransaction().commit();
        em.clear();

        assertEquals("Kotlin", em.find(Developpeur.class, dev.getId()).getLangage());
    }

    // ---------- TABLE_PER_CLASS ----------

    @Test
    public void tablePerClass_pasDeTableProduitEtUneTableParClasseConcrete() {
        enregistrer(new Livre("Clean Code", "Robert C. Martin"), new Electronique("Smartphone", 24));

        assertFalse("Aucune table pour la classe abstraite", tableExiste("Produit"));
        assertEquals(1, lignesTable("Livre"));
        assertEquals(1, lignesTable("Electronique"));
    }

    @Test
    public void tablePerClass_requetePolymorphiqueEtIdsUniques() {
        Livre livre = new Livre("Clean Code", "Robert C. Martin");
        Electronique tel = new Electronique("Smartphone", 24);
        enregistrer(livre, tel);

        List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class).getResultList();
        assertEquals(2, produits.size());

        // La séquence commune garantit des ids différents entre les deux tables
        assertNotEquals(livre.getId(), tel.getId());
        assertTrue(em.find(Produit.class, livre.getId()) instanceof Livre);
    }

    // ---------- DELETE et cas d'erreur ----------

    @Test
    public void suppression_dansChaqueStrategie() {
        Moto moto = new Moto("Harley", true);
        Manager manager = new Manager("Bob", 3);
        Electronique tel = new Electronique("Smartphone", 24);
        enregistrer(new Voiture("Toyota", 4), moto, new Developpeur("Alice", "Java"), manager,
                new Livre("Clean Code", "Robert C. Martin"), tel);

        em.getTransaction().begin();
        em.remove(em.find(Moto.class, moto.getId()));
        em.remove(em.find(Manager.class, manager.getId()));
        em.remove(em.find(Electronique.class, tel.getId()));
        em.getTransaction().commit();

        assertEquals(1, lignesTable("Vehicule"));
        assertEquals("JOINED : la ligne de la table mère est aussi supprimée", 1, lignesTable("Employe"));
        assertEquals(0, lignesTable("Manager"));
        assertEquals(0, lignesTable("Electronique"));
    }

    @Test
    public void champObligatoireManquant_leveUneExceptionEtRollback() {
        boolean exceptionLevee = false;
        try {
            em.getTransaction().begin();
            em.persist(new Voiture(null, 4)); // marque obligatoire (nullable = false)
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            exceptionLevee = true;
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        }
        assertTrue("Une voiture sans marque doit être refusée", exceptionLevee);

        // Après une erreur, on repart d'un EntityManager neuf pour vérifier la base
        em.close();
        em = emf.createEntityManager();
        assertEquals(0, lignesTable("Vehicule"));
    }
}