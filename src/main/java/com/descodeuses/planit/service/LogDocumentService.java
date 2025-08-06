package com.descodeuses.planit.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.descodeuses.planit.entity.LogDocument;

import com.descodeuses.planit.repository.LogDocumentRepository;


@Service
public class LogDocumentService {
    
    @Autowired
    private LogDocumentRepository repo;

    public void addLog(LogDocument doc) {
        repo.save(doc);
    }

}
