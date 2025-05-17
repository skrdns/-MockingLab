package edu.pzks.projtest.repository;

import edu.pzks.projtest.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    boolean existsByCode(String code);
    Item save(Item item);
    Optional<Item> findById(Long id);
    List<Item> findAll();
    void deleteById(Long id);
}
