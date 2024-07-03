package com.gateway.service;

import com.gateway.model.RouteDefinition;
import com.gateway.repository.RouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RefreshScope
public class EnrutamientoDinamicoService {
    @Autowired
    private RouteDefinitionRepository routeDefinitionRepository;

    @Autowired
    private RouteLocatorBuilder routeLocatorBuilder;

    private RouteLocatorBuilder.Builder routes;

    @Bean
    public RouteLocator dynamicRoutes() {
        routes = routeLocatorBuilder.routes();
        System.out.println("entro al been");
        updateRoutes();
        return routes.build();
    }

    public void updateRoutes() {
        System.out.println("Actualizando rutas dinámicamente...");
        routes = routeLocatorBuilder.routes();
        List<RouteDefinition> routeDefinitions = routeDefinitionRepository.findAll();
        for (RouteDefinition routeDefinition : routeDefinitions) {
            routes.route(routeDefinition.getRouteId(),
                    r -> r.path(routeDefinition.getPredicates())
                            .filters(f -> f.rewritePath(routeDefinition.getFilters(), "/${segment}"))
                            .uri(routeDefinition.getUri()));
        }
    }

}
