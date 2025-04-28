package com.kaan.WordMines.service;

import com.kaan.WordMines.exception.ExtensionException;
import com.kaan.WordMines.model.Extensions;
import com.kaan.WordMines.repo.ExtensionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtensionService {

    private final ExtensionRepo extensionRepo;

    public ExtensionService(ExtensionRepo extensionRepo) {
        this.extensionRepo = extensionRepo;
    }

    public Extensions getByGameId(Long gameId) {
        return extensionRepo.findByGameId(gameId).orElseGet(() -> null);
    }

    public void addExtension(Extensions extensions) {
        extensionRepo.save(extensions);
    }


    // her hamle basinda ve sonunda buradan extension bilgisi alinarak kullanici hakkindaki extension bilgisi alinacak ve fronendde ona gore birsey yapilacak
    public List<Extensions> getAllExtensionsByGameIdAndUserId(Long gameId, Long userId) {
        return extensionRepo.findAllByGameIdAndUserId(gameId, userId);
    }

    public void makeExtensionPassive (Long id) {
        Extensions extensions = extensionRepo.findById(id).orElseGet(() -> null) ;
        extensions.setActive(false);
        extensionRepo.save(extensions);
    }

}
