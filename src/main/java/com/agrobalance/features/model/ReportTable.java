package com.agrobalance.features.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTable {
  private String title;
  private Instant from;
  private Instant to;
  private List<String> columns = new ArrayList<>();
  private List<List<Object>> rows = new ArrayList<>();
}
