package com.kaan.WordMines.controller;

import com.kaan.WordMines.dto.GameConfirmingRequest;
import com.kaan.WordMines.dto.GameResponse;
import com.kaan.WordMines.dto.NewGameConfirmingRequest;
import com.kaan.WordMines.mapper.GameMapper;
import com.kaan.WordMines.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GameResponse> getGameById(@PathVariable Long id) {
        return ResponseEntity.ok(GameMapper.toResponse(gameService.getById(id)));
    }

    @GetMapping("/surrender")
    public ResponseEntity<String> surrender(@RequestParam Long gid, @RequestParam Long uid) {
        gameService.surrender(gid, uid);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/pass")
    public ResponseEntity<String> pass(@RequestParam Long gid, @RequestParam Long uid) {
        gameService.pass(gid, uid);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm (@RequestBody GameConfirmingRequest gameConfirmingRequest) {
        gameService.confirmChanges(gameConfirmingRequest);
        return ResponseEntity.ok("OK") ;
    }

    @PostMapping("/confirm-new-game")
    public ResponseEntity<String> confirmNewGame (@RequestBody NewGameConfirmingRequest newGameConfirmingRequest) {
        gameService.confirmNewGameRequest(newGameConfirmingRequest);
        return ResponseEntity.ok("OK") ;
    }

}
