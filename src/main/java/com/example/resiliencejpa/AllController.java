package com.example.resiliencejpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AllController {

    private final AllService allService;

    @GetMapping("/users/")
    public ResponseEntity<List<UserEntity>> all(
            @RequestParam(name = "error", required = false) boolean isError
    ) {
        try {
            if (isError) {
                return new ResponseEntity<>(allService.allError(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(allService.all(), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/")
    public ResponseEntity<UserEntity> create(@RequestBody UserEntity user) {
        return new ResponseEntity<>(allService.create(user), HttpStatus.CREATED);
    }

    @GetMapping("/sleep/")
    public ResponseEntity<Integer> sleep(
            @RequestParam(name = "delay") int delay
    ) {
        try {
            return new ResponseEntity<>(allService.sleep(delay), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sql-error/")
    public ResponseEntity<Integer> sqlError() {
        try {
            return new ResponseEntity<>(allService.sqlError(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
