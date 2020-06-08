package io.github.deposits.app.resource;

import io.github.deposits.service.dto.DepositAccountDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReportList<Entity, Parameter> {

    ResponseEntity<List<Entity>> getEntityList(Parameter parameters);
}
