package com.uca.parcialfinalncapas.service.impl;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.entities.Ticket;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.exceptions.BadTicketRequestException;
import com.uca.parcialfinalncapas.exceptions.TicketNotFoundException;
import com.uca.parcialfinalncapas.exceptions.UserNotFoundException;
import com.uca.parcialfinalncapas.repository.TicketRepository;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.security.CustomUserDetails;
import com.uca.parcialfinalncapas.service.TicketService;
import com.uca.parcialfinalncapas.utils.mappers.TicketMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TicketResponse createTicket(TicketCreateRequest request, CustomUserDetails userDetails) {
        User usuarioSolicitante = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("Usuario autenticado no encontrado"));
        User usuarioSoporte = userRepository.findByCorreo(request.getCorreoSoporte())
                .orElseThrow(() -> new UserNotFoundException("Usuario de soporte no encontrado con correo: " + request.getCorreoSoporte()));

        if (!"TECH".equals(usuarioSoporte.getNombreRol())) {
            throw new BadTicketRequestException("El usuario asignado no es un técnico de soporte");
        }

        Ticket ticket = TicketMapper.toEntityCreate(request, usuarioSolicitante.getId(), usuarioSoporte.getId());
        Ticket ticketGuardado = ticketRepository.save(ticket);
        return TicketMapper.toDTO(ticketGuardado, usuarioSolicitante.getCorreo(), usuarioSoporte.getCorreo());
    }

    @Override
    @Transactional
    public TicketResponse updateTicket(Long id, TicketUpdateRequest request) {
        Ticket ticketExistente = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket no encontrado con ID: " + id));
        User usuarioSolicitante = userRepository.findById(ticketExistente.getUsuarioId())
                .orElseThrow(() -> new UserNotFoundException("Usuario creador del ticket no encontrado"));
        User usuarioSoporte = userRepository.findByCorreo(request.getCorreoSoporte())
                .orElseThrow(() -> new UserNotFoundException("Usuario de soporte no encontrado con correo: " + request.getCorreoSoporte()));

        if (!"TECH".equals(usuarioSoporte.getNombreRol())) {
            throw new BadTicketRequestException("El usuario asignado no es un técnico de soporte");
        }

        Ticket ticketActualizado = TicketMapper.toEntityUpdate(request, usuarioSoporte.getId(), ticketExistente);
        Ticket ticketGuardado = ticketRepository.save(ticketActualizado);
        return TicketMapper.toDTO(ticketGuardado, usuarioSolicitante.getCorreo(), usuarioSoporte.getCorreo());
    }

    @Override
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException("Ticket no encontrado con ID: " + id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public TicketResponse getTicketByIdForUser(Long id, CustomUserDetails userDetails) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket no encontrado con ID: " + id));
        boolean isTech = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TECH"));

        if (!isTech && !ticket.getUsuarioId().equals(userDetails.getId())) {
            throw new AccessDeniedException("No tienes permiso para ver este ticket.");
        }

        User usuarioSolicitante = userRepository.findById(ticket.getUsuarioId())
                .orElseThrow(() -> new UserNotFoundException("Usuario creador no encontrado"));
        User usuarioSoporte = userRepository.findById(ticket.getTecnicoAsignadoId())
                .orElseThrow(() -> new UserNotFoundException("Técnico asignado no encontrado"));
        return TicketMapper.toDTO(ticket, usuarioSolicitante.getCorreo(), usuarioSoporte.getCorreo());
    }

    @Override
    public List<TicketResponse> getAllTicketsForUser(CustomUserDetails userDetails) {
        boolean isTech = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TECH"));
        List<Ticket> tickets;

        if (isTech) {
            tickets = ticketRepository.findAll();
        } else {
            tickets = ticketRepository.findByUsuarioId(userDetails.getId());
        }

        return tickets.stream().map(ticket -> {
            User solicitante = userRepository.findById(ticket.getUsuarioId()).orElse(null);
            User soporte = userRepository.findById(ticket.getTecnicoAsignadoId()).orElse(null);
            return TicketMapper.toDTO(ticket,
                    solicitante != null ? solicitante.getCorreo() : "N/A",
                    soporte != null ? soporte.getCorreo() : "N/A");
        }).collect(Collectors.toList());
    }
}