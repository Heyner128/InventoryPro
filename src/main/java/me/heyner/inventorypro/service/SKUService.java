package me.heyner.inventorypro.service;

import me.heyner.inventorypro.repository.SKURepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SKUService {

    Logger logger = LoggerFactory.getLogger(SKUService.class);

    private final SKURepository skuRepository;

    public SKUService(SKURepository skuRepository) {
        this.skuRepository = skuRepository;
    }
}
