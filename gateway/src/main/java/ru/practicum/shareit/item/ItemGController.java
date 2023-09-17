package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemGController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> saveNewItem(@Validated(Create.class) @RequestBody ItemDtoRequest itemDto,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST / items {} / user {}", itemDto.getName(), userId);
        return itemClient.saveNewItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
                                             @Validated(Update.class) @RequestBody ItemDtoRequest itemDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("PATCH / items {} / user {}", itemId, userId);
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / items {} / user {}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / items / user {}", userId);
        return itemClient.getItemsByOwner(from, size, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemBySearch(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                  @RequestParam String text,
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / search / {}", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        return itemClient.getItemBySearch(from, size, text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveNewComment(@PathVariable long itemId,
                                                 @Validated(Create.class) @RequestBody CommentDtoRequest commentDto,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST / comment / item {}", itemId);
        return itemClient.saveNewComment(itemId, commentDto, userId);
    }
}