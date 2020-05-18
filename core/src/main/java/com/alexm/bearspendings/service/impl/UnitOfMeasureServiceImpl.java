package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.entity.UnitOfMeasure;
import com.alexm.bearspendings.repository.UnitOfMeasureRepository;
import com.alexm.bearspendings.service.UnitOfMeasureService;
import org.springframework.stereotype.Service;

import static com.alexm.bearspendings.entity.UnitOfMeasure.DEFAULT_UNIT;

/**
 * @author AlexM
 * Date: 5/18/20
 **/
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository repository;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository repository) {
        this.repository = repository;
    }

    @Override
    public UnitOfMeasure defaultUnit() {
        return repository.findByName(DEFAULT_UNIT)
                .orElseGet(() -> repository.save(new UnitOfMeasure(DEFAULT_UNIT)));
    }
}
