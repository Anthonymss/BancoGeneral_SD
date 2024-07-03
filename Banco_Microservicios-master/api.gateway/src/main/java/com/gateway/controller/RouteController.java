package com.gateway.controller;

import com.gateway.config.ApplicationRestart;
import com.gateway.model.RouteDefinition;
import com.gateway.repository.RouteDefinitionRepository;
import com.gateway.service.EnrutamientoDinamicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/routes")
public class RouteController {
    @Autowired
    private RouteDefinitionRepository routeDefinitionRepository;

    @Autowired
    private EnrutamientoDinamicoService enrutamientoDinamicoService;

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ApplicationRestart applicationRestart;

    public void reiniciarAplicacion() {
        applicationRestart.restartApp();
    }

    @GetMapping
    public String getRoutes(Model model) {
        model.addAttribute("routes", routeDefinitionRepository.findAll());
        model.addAttribute("newRoute", new RouteDefinition());
        return "routes";
    }

    @PostMapping
    public String addRoute(RouteDefinition routeDefinition) {
        String routeId = routeDefinition.getRouteId();
        String uri = "lb://" + routeDefinition.getUri();
        String path = "/" + routeId + "/(?<segment>.*)";
        String predicates = "/" + routeId + "/**";

        routeDefinition.setPredicates(predicates);
        routeDefinition.setFilters(path);
        routeDefinition.setUri(uri);
        routeDefinitionRepository.save(routeDefinition);
        enrutamientoDinamicoService.updateRoutes();
        publisher.publishEvent(new RefreshRoutesEvent(this));
        reiniciarAplicacion();
        return "redirect:/routes";
    }
}
