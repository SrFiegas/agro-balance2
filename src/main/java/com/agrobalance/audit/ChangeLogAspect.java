package com.agrobalance.audit;

import com.agrobalance.config.AuditorContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ChangeLogAspect {
  private final ChangeLogRepository repo;
  private final ObjectMapper mapper = new ObjectMapper();

  @AfterReturning(pointcut = "@annotation(changeLogged)", returning = "ret", argNames = "jp,changeLogged,ret")
  public void log(JoinPoint jp, ChangeLogged changeLogged, Object ret) throws Throwable {
    String id = extractId(ret);
    String json = mapper.writeValueAsString(ret);
    repo.save(ChangeLog.builder()
        .entity(changeLogged.entity())
        .entityId(id)
        .action(changeLogged.action())
        .changes(json)
        .actor(AuditorContext.get())
        .build());
  }

  private String extractId(Object o) {
    if (o == null) return "N/A";
    try {
      var f = o.getClass().getDeclaredField("id");
      f.setAccessible(true);
      Object v = f.get(o);
      return v == null ? "N/A" : String.valueOf(v);
    } catch (Exception e) {
      return "N/A";
    }
  }
}
