package com.gateway.repository;

import com.gateway.model.RouteDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteDefinitionRepository extends JpaRepository<RouteDefinition, Long> {
}
