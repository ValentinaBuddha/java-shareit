package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestServiceImpl requestService;

    @PostMapping
    public ItemRequestDtoOut saveNewRequest(@RequestBody ItemRequestDtoIn requestDtoIn,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.saveNewRequest(requestDtoIn, userId);
    }

    @GetMapping
    public List<ItemRequestDtoOut> getRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequestsByRequestor(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllRequests(@RequestParam(defaultValue = "1") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getRequestById(@PathVariable long requestId,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequestById(requestId, userId);
    }
}
