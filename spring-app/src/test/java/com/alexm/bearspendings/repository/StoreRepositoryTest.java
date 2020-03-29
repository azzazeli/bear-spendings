package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        final Store store = Store.builder().build();
        final Store savedSTore = storeRepository.save(store);
        assertNotNull(savedSTore.getId());
        assertNotNull(savedSTore.getCreatedDT());
        assertNotNull(savedSTore.getModifiedDT());
    }
}
