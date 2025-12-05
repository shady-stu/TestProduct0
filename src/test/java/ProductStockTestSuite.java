

import org.example.ProductStock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import static org.junit.jupiter.api.Assertions.*;

// ===========================================
// MAIN TEST CLASS WITH TAGS
// ===========================================
@DisplayName("ProductStock Test Suite")
@Tag("AllTests")

public class ProductStockTestSuite {

    // ======================
    // CONSTRUCTOR TESTS
    // ======================
    @Nested
    @DisplayName("Constructor Tests")
    @Tag("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("TC-STK-001: Valid Parameters")
        @Tag("Normal")
        void constructorValidParameters() {
            ProductStock stock = new ProductStock("P01", "WH-1", 200, 20, 200);
            assertNotNull(stock);
        }

        @Test
        @DisplayName("TC-STK-002: Null Product ID")
        @Tag("Error")
        void constructorNullProductId() {
            assertThrows(IllegalArgumentException.class, () ->
                    new ProductStock(null, "WH-1", 200, 20, 201));
        }

        @Test
        @DisplayName("TC-STK-003: Null Location")
        @Tag("Error")
        void constructorNullLocation() {
            assertThrows(IllegalArgumentException.class, () ->
                    new ProductStock("P01", null, 200, 20, 202));
        }

        @Test
        @DisplayName("TC-STK-004: Negative Initial On Hand")
        @Tag("Error")
        void constructorNegativeInitialOnHand() {
            assertThrows(IllegalArgumentException.class, () ->
                    new ProductStock("P01", "WH-1", -75, 20, 203));
        }

        @Test
        @DisplayName("TC-STK-005: Negative Reorder Threshold")
        @Tag("Error")
        void constructorNegativeReorderThreshold() {
            assertThrows(IllegalArgumentException.class, () ->
                    new ProductStock("P01", "WH-1", 200, -80, 204));
        }

        @Test
        @DisplayName("TC-STK-006: Zero Max Capacity")
        @Tag("Error")
        void constructorZeroMaxCapacity() {
            assertThrows(IllegalArgumentException.class, () ->
                    new ProductStock("P01", "WH-1", 200, 80, 0));
        }

        @Test
        @DisplayName("TC-STK-007: Initial On Hand Exceeds Max Capacity")
        @Tag("Error")
        void constructorInitialOnHandExceedsMaxCapacity() {
            assertThrows(IllegalArgumentException.class, () ->
                    new ProductStock("P001", "WH-1", 300, 20, 200));
        }

        @Test
        @DisplayName("TC-STK-008: Zero Values")
        @Tag("Boundary")
        void constructorZeroValues() {
            ProductStock stock = new ProductStock("P001", "WH-1", 0, 0, 100);
            assertNotNull(stock);
        }

        @Test
        @DisplayName("TC-STK-009: At Max Capacity")
        @Tag("Boundary")
        void constructorAtMaxCapacity() {
            ProductStock stock = new ProductStock("P001", "WH-1", 200, 200, 200);
            assertNotNull(stock);
        }
    }

    // ======================
    // GET AVAILABLE TESTS
    // ======================
    @Nested
    @DisplayName("getAvailable Tests")
    @Tag("GetAvailable")
    class GetAvailableTests {

        @Test
        @DisplayName("TC-STK-010: Normal (100-30=70)")
        @Tag("Normal")
        void getAvailableNormal() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(30);
            assertEquals(70, stock.getAvailable());
        }

        @Test
        @DisplayName("TC-STK-011: Zero Available (50-50=0)")
        @Tag("Boundary")
        void getAvailableZero() {
            ProductStock stock = new ProductStock("P001", "WH-1", 50, 20, 200);
            stock.reserve(50);
            assertEquals(0, stock.getAvailable());
        }

        @Test
        @DisplayName("TC-STK-012: All Zero (0-0=0)")
        @Tag("Boundary")
        void getAvailableAllZero() {
            ProductStock stock = new ProductStock("P001", "WH-1", 0, 20, 200);
            assertEquals(0, stock.getAvailable());
        }
    }

    // ======================
    // CHANGE LOCATION TESTS
    // ======================
    @Nested
    @DisplayName("changeLocation Tests")
    @Tag("ChangeLocation")
    class ChangeLocationTests {

        private ProductStock stock;

        @BeforeEach
        void setUp() {
            stock = new ProductStock("P001", "WH-1", 100, 20, 200);
        }

        @Test
        @DisplayName("TC-STK-013: Valid Location Change")
        @Tag("Normal")
        void changeLocationValid() {
            stock.changeLocation("WH-2-A5");
            assertEquals("WH-2-A5", stock.getLocation());
        }

        @Test
        @DisplayName("TC-STK-014: Null Location")
        @Tag("Error")
        void changeLocationNull() {
            assertThrows(IllegalArgumentException.class, () -> stock.changeLocation(null));
        }

        @Test
        @DisplayName("TC-STK-015: Empty Location")
        @Tag("Error")
        void changeLocationEmpty() {
            assertThrows(IllegalArgumentException.class, () -> stock.changeLocation(""));
        }
    }

    // ======================
    // ADD STOCK TESTS
    // ======================
    @Nested
    @DisplayName("addStock Tests")
    @Tag("AddStock")
    class AddStockTests {

        @Test
        @DisplayName("TC-STK-016: Normal Add (50+30=80)")
        @Tag("Normal")
        void addStockNormal() {
            ProductStock stock = new ProductStock("P001", "WH-1", 50, 20, 100);
            stock.addStock(30);
            assertEquals(80, stock.getOnHand());
        }

        @Test
        @DisplayName("TC-STK-017: Zero Amount")
        @Tag("Error")
        void addStockZeroAmount() {
            ProductStock stock = new ProductStock("P001", "WH-1", 80, 20, 100);
            assertThrows(IllegalArgumentException.class, () -> stock.addStock(0));
        }

        @Test
        @DisplayName("TC-STK-018: Negative Amount")
        @Tag("Error")
        void addStockNegativeAmount() {
            ProductStock stock = new ProductStock("P001", "WH-1", 80, 20, 100);
            assertThrows(IllegalArgumentException.class, () -> stock.addStock(-10));
        }

        @Test
        @DisplayName("TC-STK-019: Exceeds Capacity (80+30>100)")
        @Tag("Error")
        void addStockExceedsCapacity() {
            ProductStock stock = new ProductStock("P001", "WH-1", 80, 20, 100);
            assertThrows(IllegalStateException.class, () -> stock.addStock(30));
        }

        @Test
        @DisplayName("TC-STK-020: Add to Max Capacity (0+100=100)")
        @Tag("Boundary")
        void addStockToMaxCapacity() {
            ProductStock stock = new ProductStock("P001", "WH-1", 0, 20, 100);
            stock.addStock(100);
            assertEquals(100, stock.getOnHand());
        }
    }

    // ======================
    // REMOVE DAMAGED TESTS
    // ======================
    @Nested
    @DisplayName("removeDamaged Tests")
    @Tag("RemoveDamaged")
    class RemoveDamagedTests {

        @Test
        @DisplayName("TC-STK-021: Reserved Less Than Removed")
        @Tag("Normal")
        void removeDamagedReservedLessThanRemoved() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(20);
            stock.removeDamaged(30);
            assertEquals(70, stock.getOnHand());
            assertEquals(20, stock.getReserved());
        }

        @Test
        @DisplayName("TC-STK-022: Reserved More Than Removed")
        @Tag("Normal")
        void removeDamagedReservedMoreThanRemoved() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(80);
            stock.removeDamaged(30);
            assertEquals(70, stock.getOnHand());
            assertEquals(70, stock.getReserved());
        }

        @Test
        @DisplayName("TC-STK-023: Zero Amount")
        @Tag("Error")
        void removeDamagedZeroAmount() {
            ProductStock stock = new ProductStock("P001", "WH-1", 50, 20, 200);
            assertThrows(IllegalArgumentException.class, () -> stock.removeDamaged(0));
        }

        @Test
        @DisplayName("TC-STK-024: Exceeds On Hand")
        @Tag("Error")
        void removeDamagedExceedsOnHand() {
            ProductStock stock = new ProductStock("P001", "WH-1", 50, 20, 200);
            assertThrows(IllegalStateException.class, () -> stock.removeDamaged(60));
        }

        @Test
        @DisplayName("TC-STK-025: Remove All On Hand")
        @Tag("Boundary")
        void removeDamagedAllOnHand() {
            ProductStock stock = new ProductStock("P001", "WH-1", 50, 20, 200);
            stock.removeDamaged(50);
            assertEquals(0, stock.getOnHand());
        }
    }

    // ======================
    // RESERVE TESTS
    // ======================
    @Nested
    @DisplayName("reserve Tests")
    @Tag("Reserve")
    class ReserveTests {

        @Test
        @DisplayName("TC-STK-026: Normal Reserve (100, reserve 30)")
        @Tag("Normal")
        void reserveNormal() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(30);
            assertEquals(30, stock.getReserved());
            assertEquals(70, stock.getAvailable());
        }

        @Test
        @DisplayName("TC-STK-027: Zero Amount")
        @Tag("Error")
        void reserveZeroAmount() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(40);
            assertThrows(IllegalArgumentException.class, () -> stock.reserve(0));
        }

        @Test
        @DisplayName("TC-STK-028: Exceeds Available")
        @Tag("Error")
        void reserveExceedsAvailable() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
        stock.reserve(40);
            assertThrows(IllegalStateException.class, () -> stock.reserve(70));
        }

        @Test
        @DisplayName("TC-STK-029: Reserve All Available")
        @Tag("Boundary")
        void reserveAllAvailable() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(100);
            assertEquals(100, stock.getReserved());
            assertEquals(0, stock.getAvailable());
        }
    }

    // ======================
    // RELEASE RESERVATION TESTS
    // ======================
    @Nested
    @DisplayName("releaseReservation Tests")
    @Tag("ReleaseReservation")
    class ReleaseReservationTests {

        private ProductStock stock;

        @BeforeEach
        void setUp() {
            stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(50);
        }

        @Test
        @DisplayName("TC-STK-030: Normal Release (50-30=20)")
        @Tag("Normal")
        void releaseReservationNormal() {
            stock.releaseReservation(30);
            assertEquals(20, stock.getReserved());
            assertEquals(80, stock.getAvailable());
        }

        @Test
        @DisplayName("TC-STK-031: Zero Amount")
        @Tag("Error")
        void releaseReservationZeroAmount() {
            assertThrows(IllegalArgumentException.class, () -> stock.releaseReservation(0));
        }

        @Test
        @DisplayName("TC-STK-032: Exceeds Reserved")
        @Tag("Error")
        void releaseReservationExceedsReserved() {
            assertThrows(IllegalStateException.class, () -> stock.releaseReservation(60));
        }

        @Test
        @DisplayName("TC-STK-033: Release All Reserved")
        @Tag("Boundary")
        void releaseReservationAllReserved() {
            stock.releaseReservation(50);
            assertEquals(0, stock.getReserved());
            assertEquals(100, stock.getAvailable());
        }
    }

    // ======================
    // SHIP RESERVED TESTS
    // ======================
    @Nested
    @DisplayName("shipReserved Tests")
    @Tag("ShipReserved")
    class ShipReservedTests {

        private ProductStock stock;

        @BeforeEach
        void setUp() {
            stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(50);
        }

        @Test
        @DisplayName("TC-STK-034: Normal Ship (50-30=20 reserved, 100-30=70 onHand)")
        @Tag("Normal")
        void shipReservedNormal() {
            stock.shipReserved(30);
            assertEquals(20, stock.getReserved());
            assertEquals(70, stock.getOnHand());
        }

        @Test
        @DisplayName("TC-STK-035: Zero Amount")
        @Tag("Error")
        void shipReservedZeroAmount() {
            assertThrows(IllegalArgumentException.class, () -> stock.shipReserved(0));
        }

        @Test
        @DisplayName("TC-STK-036: Exceeds Reserved")
        @Tag("Error")
        void shipReservedExceedsReserved() {
            assertThrows(IllegalStateException.class, () -> stock.shipReserved(60));
        }

        @Test
        @DisplayName("TC-STK-037: Ship All Reserved")
        @Tag("Boundary")
        void shipReservedAllReserved() {
            stock.shipReserved(50);
            assertEquals(0, stock.getReserved());
            assertEquals(50, stock.getOnHand());
        }
    }

    // ======================
    // IS REORDER NEEDED TESTS
    // ======================
    @Nested
    @DisplayName("isReorderNeeded Tests")
    @Tag("IsReorderNeeded")
    class IsReorderNeededTests {

        @Test
        @DisplayName("TC-STK-038: Not Needed (100-30=70 > 50)")
        @Tag("Normal")
        void isReorderNeededFalse() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 50, 200);
            stock.reserve(30);
            assertFalse(stock.isReorderNeeded());
        }

        @Test
        @DisplayName("TC-STK-039: Needed (100-60=40 < 50)")
        @Tag("Normal")
        void isReorderNeededTrue() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 50, 200);
            stock.reserve(60);
            assertTrue(stock.isReorderNeeded());
        }

        @Test
        @DisplayName("TC-STK-040: At Threshold (100-50=50 == 50)")
        @Tag("Boundary")
        void isReorderNeededAtThreshold() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 50, 200);
            stock.reserve(50);
            assertFalse(stock.isReorderNeeded());
        }
    }

    // ======================
    // UPDATE REORDER THRESHOLD TESTS
    // ======================
    @Nested
    @DisplayName("updateReorderThreshold Tests")
    @Tag("UpdateReorderThreshold")
    class UpdateReorderThresholdTests {

        private ProductStock stock;

        @BeforeEach
        void setUp() {
            stock = new ProductStock("P001", "WH-1", 100, 20, 200);
        }

        @Test
        @DisplayName("TC-STK-041: Normal Update (20→30)")
        @Tag("Normal")
        void updateReorderThresholdNormal() {
            stock.updateReorderThreshold(30);
            assertEquals(30, stock.getReorderThreshold());
        }

        @Test
        @DisplayName("TC-STK-042: To Zero")
        @Tag("Boundary")
        void updateReorderThresholdToZero() {
            stock.updateReorderThreshold(0);
            assertEquals(0, stock.getReorderThreshold());
        }

        @Test
        @DisplayName("TC-STK-043: To Max Capacity")
        @Tag("Boundary")
        void updateReorderThresholdToMaxCapacity() {
            stock.updateReorderThreshold(200);
            assertEquals(200, stock.getReorderThreshold());
        }

        @Test
        @DisplayName("TC-STK-044: Negative")
        @Tag("Error")
        void updateReorderThresholdNegative() {
            assertThrows(IllegalArgumentException.class, () -> stock.updateReorderThreshold(-10));
        }

        @Test
        @DisplayName("TC-STK-045: Exceeds Max Capacity")
        @Tag("Error")
        void updateReorderThresholdExceedsMaxCapacity() {
            assertThrows(IllegalArgumentException.class, () -> stock.updateReorderThreshold(250));
        }
    }

    // ======================
    // UPDATE MAX CAPACITY TESTS
    // ======================
    @Nested
    @DisplayName("updateMaxCapacity Tests")
    @Tag("UpdateMaxCapacity")
    class UpdateMaxCapacityTests {

        @Test
        @DisplayName("TC-STK-046: Threshold Less Than New Capacity")
        @Tag("Normal")
        void updateMaxCapacityNewThresholdLessThanNewCapacity() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 150, 200);
            stock.updateMaxCapacity(250);
            assertEquals(250, stock.getMaxCapacity());
            assertEquals(150, stock.getReorderThreshold());
        }

        @Test
        @DisplayName("TC-STK-047: Threshold Greater Than New Capacity")
        @Tag("Normal")
        void updateMaxCapacityNewThresholdGreaterThanNewCapacity() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 250, 200);
            stock.updateMaxCapacity(300);
            assertEquals(300, stock.getMaxCapacity());
            assertEquals(250, stock.getReorderThreshold());
        }

        @Test
        @DisplayName("TC-STK-048: Below Current On Hand")
        @Tag("Error")
        void updateMaxCapacityBelowCurrentOnHand() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            assertThrows(IllegalStateException.class, () -> stock.updateMaxCapacity(50));
        }

        @Test
        @DisplayName("TC-STK-049: Zero")
        @Tag("Error")
        void updateMaxCapacityZero() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            assertThrows(IllegalArgumentException.class, () -> stock.updateMaxCapacity(0));
        }

        @Test
        @DisplayName("TC-STK-050: To Current On Hand")
        @Tag("Boundary")
        void updateMaxCapacityToCurrentOnHand() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.updateMaxCapacity(100);
            assertEquals(100, stock.getMaxCapacity());
        }
    }

    // ======================
    // TO STRING TEST
    // ======================
    @Nested
    @DisplayName("toString Test")
    @Tag("ToString")
    class ToStringTest {

        @Test
        @DisplayName("TC-STK-051: toString Format")
        @Tag("Normal")
        void toStringTest() {
            ProductStock stock = new ProductStock("P001", "WH-1", 100, 20, 200);
            stock.reserve(30);
            String result = stock.toString();

            assertNotNull(result);
            assertTrue(result.contains("P001"), "Should contain product ID");
            assertTrue(result.contains("WH-1"), "Should contain location");
            assertTrue(result.contains("100"), "Should contain onHand");
            assertTrue(result.contains("30"), "Should contain reserved");
            assertTrue(result.contains("20"), "Should contain reorderThreshold");
            assertTrue(result.contains("200"), "Should contain maxCapacity");
        }
    }





    // ===========================================
// COMPLETE TEST SUITE (ALL TESTS)
// ===========================================
    @Suite
    @SuiteDisplayName("ProductStock Complete Test Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags("AllTests")
    public class AllTestsSuite {
    }

    // ===========================================
// NORMAL TESTS SUITE
// ===========================================
    @Suite
    @SuiteDisplayName("Normal Tests Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags("Normal")
    public class NormalTestsSuite {
    }

    // ===========================================
// ERROR TESTS SUITE
// ===========================================
    @Suite
    @SuiteDisplayName("Error Tests Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags("Error")
    public class ErrorTestsSuite {
    }

    // ===========================================
// BOUNDARY TESTS SUITE
// ===========================================
    @Suite
    @SuiteDisplayName("Boundary Tests Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags("Boundary")
    public class BoundaryTestsSuite {
    }

    // ===========================================
// FUNCTIONAL TEST SUITES
// ===========================================
    @Suite
    @SuiteDisplayName("Constructor Tests Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags("Constructor")
    public class ConstructorTestsSuite {
    }

    @Suite
    @SuiteDisplayName("Stock Operations Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags({"AddStock", "RemoveDamaged", "Reserve", "ReleaseReservation", "ShipReserved"})
    public class StockOperationsSuite {
    }

    @Suite
    @SuiteDisplayName("Configuration Tests Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags({"UpdateReorderThreshold", "UpdateMaxCapacity", "ChangeLocation"})
    public class ConfigurationTestsSuite {

    }

    @Suite
    @SuiteDisplayName("Query Tests Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags({"GetAvailable", "IsReorderNeeded", "ToString"})
    public class QueryTestsSuite {
    }

    // ===========================================
// SMOKE TEST SUITE (QUICK TEST)
// ===========================================
    @Suite
    @SuiteDisplayName("Smoke Test Suite")
    @SelectClasses(ProductStockTestSuite.class)
    @IncludeTags({"Normal", "Constructor"})
    public class SmokeTestSuite {
    }
}