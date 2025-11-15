package com.agrobalance.features.service.impl;

import com.agrobalance.exception.BusinessException;
import com.agrobalance.exception.NotFoundException;
import com.agrobalance.features.domain.Pessoa;
import com.agrobalance.features.domain.Enum.PessoaTipo;
import com.agrobalance.features.dto.PessoaCreateDto;
import com.agrobalance.features.dto.PessoaResponseDto;
import com.agrobalance.features.dto.PessoaUpdateDto;
import com.agrobalance.features.mapper.PessoaMapper;
import com.agrobalance.features.repository.PessoaRepository;
import com.agrobalance.features.service.PessoaService;
import com.agrobalance.features.repository.FazendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PessoaServiceImpl implements PessoaService {

  private final PessoaRepository repository;
  private final PessoaMapper mapper;
  private final FazendaRepository fazendaRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<PessoaResponseDto> list(PessoaTipo tipo, String q, Pageable pageable) {
    Page<Pessoa> page;
    boolean hasQ = q != null && !q.isBlank();
    if (tipo != null && hasQ) {
      page = repository.findByTipoAndNomeContainingIgnoreCase(tipo, q, pageable);
    } else if (tipo != null) {
      page = repository.findByTipo(tipo, pageable);
    } else if (hasQ) {
      page = repository.findByNomeContainingIgnoreCase(q, pageable);
    } else {
      page = repository.findAll(pageable);
    }
    List<PessoaResponseDto> content = page.getContent().stream().map(mapper::toDto).toList();
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public PessoaResponseDto get(Long id) {
    Pessoa p = repository.findById(id).orElseThrow(() -> new NotFoundException("Pessoa não encontrada"));
    return mapper.toDto(p);
  }

  @Override
  public PessoaResponseDto create(PessoaCreateDto dto) {
    if (repository.existsByNomeIgnoreCase(dto.getNome())) {
      throw new BusinessException("Já existe pessoa com este nome");
    }
    Pessoa p = mapper.toEntity(dto);

    // Garantir consistência da relação com fazenda (valida existência)
    if (dto.getFazendaId() != null) {
      if (dto.getFazendaId() == 0) {
        p.setFazenda(null);
      } else {
        p.setFazenda(
            fazendaRepository.findById(dto.getFazendaId())
                .orElseThrow(() -> new NotFoundException("Fazenda inválida"))
        );
      }
    }

    repository.save(p);
    return mapper.toDto(p);
  }

  @Override
  public PessoaResponseDto update(Long id, PessoaUpdateDto dto) {
    Pessoa p = repository.findById(id).orElseThrow(() -> new NotFoundException("Pessoa não encontrada"));

    // Verifica duplicidade de nome se for alterar
    if (dto.getNome() != null && !dto.getNome().equalsIgnoreCase(p.getNome())
        && repository.existsByNomeIgnoreCase(dto.getNome())) {
      throw new BusinessException("Já existe pessoa com este nome");
    }

    // Atualiza campos simples parcialmente
    if (dto.getNome() != null) p.setNome(dto.getNome());
    if (dto.getDocumento() != null) p.setDocumento(dto.getDocumento());
    if (dto.getEmail() != null) p.setEmail(dto.getEmail());
    if (dto.getTelefone() != null) p.setTelefone(dto.getTelefone());
    if (dto.getCnh() != null) p.setCnh(dto.getCnh());
    if (dto.getTipo() != null) p.setTipo(dto.getTipo());
    if (dto.getStatus() != null) p.setStatus(dto.getStatus());

    // Atualiza relação somente se informado explicitamente
    if (dto.getFazendaId() != null) {
      p.setFazenda(dto.getFazendaId() == 0 ? null :
          fazendaRepository.findById(dto.getFazendaId()).orElseThrow(() -> new NotFoundException("Fazenda inválida")));
    }

    p = repository.saveAndFlush(p);
    return mapper.toDto(p);
  }

  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) throw new NotFoundException("Pessoa não encontrada");
    repository.deleteById(id);
  }
}
