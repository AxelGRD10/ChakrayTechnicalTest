package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSortFieldTest {

    @Test
    void testEnumValuesExist() {
        UserSortField[] expected = {
                UserSortField.email,
                UserSortField.id,
                UserSortField.name,
                UserSortField.phone,
                UserSortField.tax_id,
                UserSortField.created_at
        };

        UserSortField[] actual = UserSortField.values();

        assertArrayEquals(expected, actual);
    }

    @Test
    void testValueOf() {
        assertEquals(UserSortField.email, UserSortField.valueOf("email"));
        assertEquals(UserSortField.id, UserSortField.valueOf("id"));
        assertEquals(UserSortField.name, UserSortField.valueOf("name"));
        assertEquals(UserSortField.phone, UserSortField.valueOf("phone"));
        assertEquals(UserSortField.tax_id, UserSortField.valueOf("tax_id"));
        assertEquals(UserSortField.created_at, UserSortField.valueOf("created_at"));
    }

    @Test
    void testEnumCount() {
        assertEquals(6, UserSortField.values().length);
    }
}