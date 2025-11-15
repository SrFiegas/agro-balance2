package com.agrobalance.features.service.impl;

import com.agrobalance.exception.NotFoundException;
import com.agrobalance.features.domain.OperacaoMovimento;
import com.agrobalance.features.dto.OperacaoMovimentoCreateDto;
import com.agrobalance.features.dto.OperacaoMovimentoResponseDto;
import com.agrobalance.features.mapper.OperacaoMovimentoMapper;
import com.agrobalance.features.repository.OperacaoMovimentoRepository;
import com.agrobalance.features.service.OperacaoMovimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OperacaoMovimentoServiceImpl implements OperacaoMovimentoService {
  private final OperacaoMovimentoRepository repository;
  private final OperacaoMovimentoMapper mapper;

  @Override
  public OperacaoMovimentoResponseDto create(OperacaoMovimentoCreateDto dto) {
    OperacaoMovimento e = mapper.toEntity(dto);

    // Regra básica: calcular peso_liquido se possível (bruto - tara), sem descontos ainda
    if (e.getPesoBruto() != null && e.getTara() != null) {
      BigDecimal liquido = e.getPesoBruto().subtract(e.getTara());
      if (liquido.signum() < 0) liquido = BigDecimal.ZERO;
      e.setPesoLiquido(liquido);
    }
    if (e.getDataRegistro() == null) {
      e.setDataRegistro(Instant.now());
    }

    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  @Transactional(readOnly = true)
  public OperacaoMovimentoResponseDto get(Long id) {
    OperacaoMovimento e = repository.findById(id).orElseThrow(() -> new NotFoundException("Operação não encontrada"));
    return mapper.toDto(e);
  }

  @Override
  @Transactional(readOnly = true)
  public List<OperacaoMovimentoResponseDto> search(Instant from, Instant to, Long cooperadoId, Long produtoId) {
    if (from == null) from = Instant.EPOCH;
    if (to == null) to = Instant.now();
    return repository.search(from, to, cooperadoId, produtoId).stream().map(mapper::toDto).toList();
  }
}
