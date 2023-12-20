package com.example.pdffiller.repository;

import com.example.pdffiller.entity.PdfInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PdfInfoRepository extends JpaRepository<com.example.pdffiller.entity.PdfInfo, Long> {

    List<PdfInfo> findAllByFilledIsFalse();
}
