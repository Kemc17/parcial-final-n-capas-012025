package com.uca.parcialfinalncapas.service;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.security.CustomUserDetails;

import java.util.List;

public interface TicketService {
    TicketResponse createTicket(TicketCreateRequest ticket, CustomUserDetails userDetails);
    TicketResponse updateTicket(Long id, TicketUpdateRequest ticket);
    void deleteTicket(Long id);
    TicketResponse getTicketByIdForUser(Long id, CustomUserDetails userDetails);
    List<TicketResponse> getAllTicketsForUser(CustomUserDetails userDetails);
}