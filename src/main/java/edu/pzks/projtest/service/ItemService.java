package edu.pzks.projtest.service;

import edu.pzks.projtest.model.Item;
import edu.pzks.projtest.repository.ItemRepository;
import edu.pzks.projtest.request.ItemCreateRequest;
import edu.pzks.projtest.request.ItemUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(ItemCreateRequest request) {
        if (itemRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("Item with code already exists");
        }

        Item item = Item.builder()
                .name(request.name())
                .code(request.code())
                .description(request.description())
                .createDate(LocalDateTime.now())
                .updateDate(Optional.empty())
                .build();

        return itemRepository.save(item);
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public Item update(Long id, ItemUpdateRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        item.setName(request.name());
        item.setDescription(request.description());
        item.setUpdateDate(Optional.of(LocalDateTime.now()));

        return itemRepository.save(item);
    }
}
