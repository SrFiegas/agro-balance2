package com.agrobalance.integration.scale;

import java.time.Instant;

public record ScaleReading(double value, String unit, boolean stable, Instant timestamp) {}
