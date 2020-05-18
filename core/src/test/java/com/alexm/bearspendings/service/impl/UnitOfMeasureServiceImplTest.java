package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.entity.UnitOfMeasure;
import com.alexm.bearspendings.repository.UnitOfMeasureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.alexm.bearspendings.entity.UnitOfMeasure.DEFAULT_UNIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author AlexM
 * Date: 5/18/20
 **/
@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository repository;
    @InjectMocks
    UnitOfMeasureServiceImpl service;


    @Test
    void createDefaultStore() {
        service.defaultUnit();
        verify(repository).save(any(UnitOfMeasure.class));
    }

    @Test
    void returnExistingDefaultSTore() {
        final UnitOfMeasure value = new UnitOfMeasure(DEFAULT_UNIT);
        when(repository.findByName(DEFAULT_UNIT)).thenReturn(Optional.of(value));
        assertEquals(value, service.defaultUnit());
    }

}
