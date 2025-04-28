package com.kaan.WordMines.controller;

import com.kaan.WordMines.dto.GameTableResponse;
import com.kaan.WordMines.mapper.GameTableMapper;
import com.kaan.WordMines.model.GameTable;
import com.kaan.WordMines.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api.table")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GameTableResponse> getTableById(@PathVariable Long id) {
        GameTable gameTable = tableService.getById(id);
        return ResponseEntity.ok(GameTableMapper.toResponse(gameTable));
    }

}
