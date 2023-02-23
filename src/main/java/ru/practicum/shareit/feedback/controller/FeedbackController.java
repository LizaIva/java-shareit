package ru.practicum.shareit.feedback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.service.impl.FeedbackServiceImpl;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {
    private final FeedbackServiceImpl feedbackService;

    @PostMapping
    public FeedbackDto create(@RequestBody @Valid FeedbackDto feedbackDto) {
        log.info("Получен запрос на создание отзыва");
        return feedbackService.put(feedbackDto);
    }

    @GetMapping("/{feedbackId}")
    public FeedbackDto getItemById(@PathVariable Integer feedbackId) {
        log.info("Получен запрос отзыва с id = {}", feedbackId);
        return feedbackService.getFeedbackById(feedbackId);
    }
}
