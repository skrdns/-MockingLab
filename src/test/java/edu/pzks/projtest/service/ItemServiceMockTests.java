package edu.pzks.projtest.service;

import edu.pzks.projtest.model.Item;
import edu.pzks.projtest.repository.ItemRepository;
import edu.pzks.projtest.request.ItemCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceMockTests {

    @Mock
    private ItemRepository mockRepository;

    @InjectMocks
    private ItemService underTest;

    @Captor
    private ArgumentCaptor<Item> argumentCaptor;

    //1 - Перевірка створення Item, якщо код унікальний
    @Test
    void shouldCreateItemWhenCodeIsUnique() {
        ItemCreateRequest request = new ItemCreateRequest("BMW", "bmw-1", "German car");
        given(mockRepository.existsByCode("bmw-1")).willReturn(false);
        given(mockRepository.save(any())).willAnswer(i -> i.getArgument(0));

        Item result = underTest.create(request);

        verify(mockRepository).save(argumentCaptor.capture());
        Item saved = argumentCaptor.getValue();

        assertEquals("BMW", saved.getName());
        assertThat(saved.getCreateDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertTrue(saved.getUpdateDate().isEmpty());

        assertSame(saved, result);
    }
    //2 - Перевірка викидання виключення при існуючому коді
    @Test
    void shouldThrowWhenCodeAlreadyExists() {
        ItemCreateRequest request = new ItemCreateRequest("Tesla", "tesla-1", "Electric car");
        given(mockRepository.existsByCode("tesla-1")).willReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.create(request));

        assertEquals("Item with code already exists", exception.getMessage());
        verify(mockRepository, never()).save(any());
    }
    //3 - Перевірка правильності збережених полів code і description
    @Test
    void shouldCaptureCorrectFieldsOnSave() {
        ItemCreateRequest request = new ItemCreateRequest("Audi", "audi-1", "Luxury car");
        given(mockRepository.existsByCode("audi-1")).willReturn(false);
        given(mockRepository.save(any())).willAnswer(i -> i.getArgument(0));

        underTest.create(request);

        verify(mockRepository).save(argumentCaptor.capture());
        Item saved = argumentCaptor.getValue();

        assertEquals("audi-1", saved.getCode());
        assertEquals("Luxury car", saved.getDescription());
    }
    //4 - Перевірка, що createDate встановлений під час створення
    @Test
    void shouldSetCreateDateOnCreate() {
        ItemCreateRequest request = new ItemCreateRequest("Ford", "ford-1", "USA car");
        given(mockRepository.existsByCode("ford-1")).willReturn(false);
        given(mockRepository.save(any())).willAnswer(i -> i.getArgument(0));

        underTest.create(request);

        verify(mockRepository).save(argumentCaptor.capture());
        Item saved = argumentCaptor.getValue();

        assertNotNull(saved.getCreateDate());
    }
    //5 - Перевірка, що updateDate порожній при створенні Item
    @Test
    void shouldNotSetUpdateDateInitially() {
        ItemCreateRequest request = new ItemCreateRequest("Honda", "honda-1", "Japan car");
        given(mockRepository.existsByCode("honda-1")).willReturn(false);
        given(mockRepository.save(any())).willAnswer(i -> i.getArgument(0));

        underTest.create(request);

        verify(mockRepository).save(argumentCaptor.capture());
        Item saved = argumentCaptor.getValue();

        assertTrue(saved.getUpdateDate().isEmpty());
    }
    //6 - Перевірка, що existsByCode викликається один раз
    @Test
    void shouldCallExistsByCodeOnce() {
        ItemCreateRequest request = new ItemCreateRequest("VW", "vw-1", "German compact");
        given(mockRepository.existsByCode("vw-1")).willReturn(false);
        given(mockRepository.save(any())).willAnswer(i -> i.getArgument(0));

        underTest.create(request);

        verify(mockRepository, times(1)).existsByCode("vw-1");
    }
    //7 - Перевірка, що save викликається один раз
    @Test
    void shouldCallSaveOnce() {
        ItemCreateRequest request = new ItemCreateRequest("Mazda", "mazda-1", "Zoom zoom");
        given(mockRepository.existsByCode("mazda-1")).willReturn(false);
        given(mockRepository.save(any())).willAnswer(i -> i.getArgument(0));

        underTest.create(request);

        verify(mockRepository, times(1)).save(any(Item.class));
    }
    //8 - Перевірка, що save не викликається, якщо код вже існує
    @Test
    void shouldNotSaveIfCodeExists() {
        ItemCreateRequest request = new ItemCreateRequest("Kia", "kia-1", "Korean");
        given(mockRepository.existsByCode("kia-1")).willReturn(true);

        try {
            underTest.create(request);
        } catch (IllegalArgumentException ignored) {}

        verify(mockRepository, never()).save(any(Item.class));
    }
    //9 - Перевірка, що збережений Item має правильне поле name
    @Test
    void savedItemShouldHaveCorrectName() {
        ItemCreateRequest request = new ItemCreateRequest("Skoda", "skoda-1", "Czech");
        given(mockRepository.existsByCode("skoda-1")).willReturn(false);
        given(mockRepository.save(any())).willAnswer(i -> i.getArgument(0));

        underTest.create(request);

        verify(mockRepository).save(argumentCaptor.capture());
        assertEquals("Skoda", argumentCaptor.getValue().getName());
    }
    //10 - Перевірка, що createDate не пізніший за поточний часgit init
    @Test
    void createDateShouldBeBeforeNow() {
        ItemCreateRequest request = new ItemCreateRequest("Renault", "renault-1", "French");
        given(mockRepository.existsByCode("renault-1")).willReturn(false);
        given(mockRepository.save(any())).willAnswer(i -> i.getArgument(0));

        underTest.create(request);

        verify(mockRepository).save(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue().getCreateDate().isBefore(LocalDateTime.now())
                || argumentCaptor.getValue().getCreateDate().isEqual(LocalDateTime.now()));
    }
}
