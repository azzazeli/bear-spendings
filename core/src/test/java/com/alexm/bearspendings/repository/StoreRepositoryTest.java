package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static com.alexm.bearspendings.service.impl.StoreServiceImpl.DEFAULT_STORE_NAME;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author AlexM
 * Date: 3/29/20
 **/

@DataJpaTest
@Import(RepoConfiguration.class)
public class StoreRepositoryTest {
    @Autowired
    StoreRepository storeRepository;

    @Test
    void createdDtExists() {
        final Store store = Store.builder().name("sample store").build();
        final Store savedSTore = storeRepository.save(store);
        assertNotNull(savedSTore.getId());
        assertNotNull(savedSTore.getCreatedDT());
        assertNotNull(savedSTore.getModifiedDT());
    }

    @Test
    void findByName() {
        final Optional<Store> byName = storeRepository.findByName(DEFAULT_STORE_NAME);
        assertNotNull(byName.orElseThrow());
        assertEquals(1L, byName.orElseThrow().getId().longValue());
        assertFalse(storeRepository.findByName("Nr.2").isPresent());
    }
}
