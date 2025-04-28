package com.kaan.WordMines.controller;

import com.kaan.WordMines.model.Extensions;
import com.kaan.WordMines.service.ExtensionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/extension")
public class ExtensionController {

    private final ExtensionService extensionService;

    public ExtensionController(ExtensionService extensionService) {
        this.extensionService = extensionService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Extensions>> getAllExtensionsByGameIdAndUserId(@RequestParam Long gid, @RequestParam Long uid) {
        return ResponseEntity.ok(extensionService.getAllExtensionsByGameIdAndUserId(gid, uid));
    }

    @PutMapping("/make-passive/{id}")
    public ResponseEntity<String> makePassive(@PathVariable Long id) {
        extensionService.makeExtensionPassive(id);
        return ResponseEntity.ok("OK");
    }
}
