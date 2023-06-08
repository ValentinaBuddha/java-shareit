package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.utils.Create;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDtoOut saveNewRequest(@Validated(Create.class) @RequestBody ItemRequestDtoIn requestDtoIn,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST / requests {} / user {}", requestDtoIn.getDescription(), userId);
        return requestService.saveNewRequest(requestDtoIn, userId);
    }

    @GetMapping
    public List<ItemRequestDtoOut> getRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / requests / requestor {}", userId);
        return requestService.getRequestsByRequestor(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllRequests(@RequestParam(defaultValue = "1") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / requests");
        return requestService.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getRequestById(@PathVariable long requestId,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET / request {} / user {}", requestId, userId);
        return requestService.getRequestById(requestId, userId);
    }
}
