package com.agrobalance.features.service;

import com.agrobalance.features.domain.Enum.PessoaTipo;
import com.agrobalance.features.dto.PessoaCreateDto;
import com.agrobalance.features.dto.PessoaResponseDto;
import com.agrobalance.features.dto.PessoaUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PessoaService {
  Page<PessoaResponseDto> list(PessoaTipo tipo, String q, Pageable pageable);
  PessoaResponseDto get(Long id);
  PessoaResponseDto create(PessoaCreateDto dto);
  PessoaResponseDto update(Long id, PessoaUpdateDto dto);
  void delete(Long id);
}
