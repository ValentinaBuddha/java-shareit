package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDtoOut;
import ru.practicum.shareit.item.comment.CommentDtoIn;
import ru.practicum.shareit.utils.Create;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto saveNewItem(@Validated(Create.class) @RequestBody ItemDto itemDto,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST / items {} / user {}", itemDto.getName(), userId);
        return itemService.saveNewItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("PATCH / items {} / user {}", itemId, userId);
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / items {} / user {}", itemId, userId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / items / user {}", userId);
        return itemService.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getFilmBySearch(@RequestParam String text) {
        log.info("GET / search / {}", text);
        return itemService.getItemBySearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(@PathVariable long itemId,
                                    @Validated(Create.class) @RequestBody CommentDtoIn commentDtoIn,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST / comment / item {}", itemId);
        return itemService.saveNewComment(itemId, commentDtoIn, userId);
    }
}
