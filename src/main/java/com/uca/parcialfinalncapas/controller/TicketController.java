package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.security.CustomUserDetails;
import com.uca.parcialfinalncapas.service.TicketService;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'TECH')")
    public ResponseEntity<GeneralResponse> getAllTickets(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TicketResponse> tickets = ticketService.getAllTicketsForUser(userDetails);
        return ResponseBuilderUtil.buildResponse("Tickets obtenidos correctamente", HttpStatus.OK, tickets);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'TECH')")
    public ResponseEntity<GeneralResponse> getTicketById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        TicketResponse ticket = ticketService.getTicketByIdForUser(id, userDetails);
        return ResponseBuilderUtil.buildResponse("Ticket encontrado", HttpStatus.OK, ticket);
    }
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GeneralResponse> createTicket(@Valid @RequestBody TicketCreateRequest ticket, @AuthenticationPrincipal CustomUserDetails userDetails) {
        TicketResponse createdTicket = ticketService.createTicket(ticket, userDetails);
        return ResponseBuilderUtil.buildResponse("Ticket creado correctamente", HttpStatus.CREATED, createdTicket);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketUpdateRequest ticket) {
        TicketResponse updatedTicket = ticketService.updateTicket(id, ticket);
        return ResponseBuilderUtil.buildResponse("Ticket actualizado correctamente", HttpStatus.OK, updatedTicket);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseBuilderUtil.buildResponse("Ticket eliminado correctamente", HttpStatus.OK, null);
    }
}
