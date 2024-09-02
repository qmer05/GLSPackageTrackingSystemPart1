package app.daos;

import app.config.HibernateConfig;
import app.entities.Package;
import app.enums.DeliveryStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PackageDAOTest {

    private static EntityManagerFactory emf;
    private static PackageDAO packageDao;

    Package p1, p2, p3, p4, p5;

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTestMode(true);
        emf = HibernateConfig.getEntityManagerFactory();
        packageDao = new PackageDAO(emf);

    }

    @AfterAll
    static void tearDownAll() {
        HibernateConfig.setTestMode(false);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Package("1234567890", "Alice Johnson", "Bob Smith", DeliveryStatus.IN_TRANSIT);
        p2 = new Package("0987654321", "John Doe", "Jane Doe", DeliveryStatus.DELIVERED);
        p3 = new Package("1122334455", "Charlie Brown", "Lucy Van Pelt", DeliveryStatus.PENDING);
        p4 = new Package("5566778899", "Michael Scott", "Dwight Schrute", DeliveryStatus.IN_TRANSIT);
        p5 = new Package("6677889900", "Tony Stark", "Pepper Potts", DeliveryStatus.DELIVERED);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Package").executeUpdate();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.persist(p5);
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createPackage() {
        // Arrange
        Package actual = new Package("3344668899", "Natasha Romanoff", "Clint Barton", DeliveryStatus.DELIVERED);
        // Act
        Package expected = packageDao.createPackage(actual);
        // Assert
        assertNotNull(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getPackage() {
        // Arrange
        Package actual = p2;
        // Act
        Package expected = packageDao.getPackage(p2.getTrackingNumber());
        // Assert
        assertNotNull(expected);
        assertEquals(actual, expected);
    }

    @Test
    void updateDeliveryStatus() {
        // Arrange
        Package actual = p3;
        // Act
        Package expected = packageDao.updateDeliveryStatus(p3.getTrackingNumber(), DeliveryStatus.IN_TRANSIT);
        // Assert
        assertNotEquals(actual, expected);
        assertNotEquals(expected.getDeliveryStatus(), actual.getDeliveryStatus());
        assertEquals(expected.getTrackingNumber(), actual.getTrackingNumber());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getReceiverName(), actual.getReceiverName());
        assertEquals(expected.getSenderName(), actual.getSenderName());
    }

    @Test
    void deletePackage() {
        // Arrange
        Package actual = p1;
        // Act
        Package expected = packageDao.deletePackage(actual.getTrackingNumber());
        // Assert
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(actual.getTrackingNumber(), expected.getTrackingNumber());
        assertThrows(NoResultException.class, () -> packageDao.deletePackage(actual.getTrackingNumber()));
    }
}