package org.ormi.priv.tfa.orderflow.kernel;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    @DisplayName("create should return a valid active product for valid inputs")
    void create_validProduct_shouldReturnActiveProduct() {
        SkuId sku = new SkuId("ABC-12345");
        Product p = assertDoesNotThrow(() -> Product.create("Name", "Description", sku));

        assertNotNull(p);
        assertEquals(ProductLifecycle.ACTIVE, p.getStatus());
        assertEquals("Name", p.getName());
        assertEquals("Description", p.getDescription());
        assertEquals(sku, p.getSkuId());
        assertEquals(1L, p.getVersion());
    }

    @Test
    @DisplayName("create should throw when inputs are invalid")
    void create_invalidInputs_shouldThrow() {
        SkuId sku = new SkuId("ABC-12345");

        assertThrows(ConstraintViolationException.class, () -> Product.create(null, "desc", sku));
        assertThrows(ConstraintViolationException.class, () -> Product.create("", "desc", sku));
        assertThrows(ConstraintViolationException.class, () -> Product.create("Name", null, sku));
        assertThrows(ConstraintViolationException.class, () -> Product.create("Name", "desc", null));
    }

    @Test
    @DisplayName("updating name/description with invalid values should throw ConstraintViolationException")
    void update_withInvalidInputs_shouldThrowConstraintViolation() {
        Product p = Product.create("Name", "Description", new SkuId("ABC-12345"));

        assertThrows(ConstraintViolationException.class, () -> p.updateName(null));
        assertThrows(ConstraintViolationException.class, () -> p.updateName(""));
        assertThrows(ConstraintViolationException.class, () -> p.updateDescription(null));
    }

    @Test
    @DisplayName("updating name on an active product should succeed and increment version")
    void update_onActiveProduct_shouldSucceed() {
        Product p = Product.create("Name", "Description", new SkuId("ABC-12345"));
        long beforeVersion = p.getVersion();

        assertDoesNotThrow(() -> p.updateName("NewName"));

        assertEquals("NewName", p.getName());
        assertEquals(ProductLifecycle.ACTIVE, p.getStatus());
        assertEquals(beforeVersion + 1, p.getVersion());
    }

    @Test
    @DisplayName("updating name on a retired product should throw IllegalStateException")
    void update_onRetiredProduct_shouldThrowIllegalStateException() {
        Product p = Product.Builder()
                .id(org.ormi.priv.tfa.orderflow.kernel.product.ProductId.newId())
                .name("Name")
                .description("Desc")
                .skuId(new SkuId("ABC-12345"))
                .status(ProductLifecycle.RETIRED)
                .version(1L)
                .build();

        assertThrows(IllegalStateException.class, () -> p.updateName("NewName"));
        assertThrows(IllegalStateException.class, () -> p.updateDescription("NewDesc"));
    }

    @Test
    @DisplayName("retire on an active product should succeed and set status to RETIRED")
    void retire_onActiveProduct_shouldSucceed() {
        Product p = Product.create("Name", "Description", new SkuId("ABC-12345"));

        assertDoesNotThrow(() -> p.retire());

        assertEquals(ProductLifecycle.RETIRED, p.getStatus());
        assertEquals(2L, p.getVersion());
    }

    @Test
    @DisplayName("retire on an already retired product should throw IllegalStateException")
    void retire_onRetiredProduct_shouldThrowIllegalStateException() {
        Product p = Product.Builder()
                .id(org.ormi.priv.tfa.orderflow.kernel.product.ProductId.newId())
                .name("Name")
                .description("Desc")
                .skuId(new SkuId("ABC-12345"))
                .status(ProductLifecycle.RETIRED)
                .version(1L)
                .build();

        assertThrows(IllegalStateException.class, p::retire);
    }
}
