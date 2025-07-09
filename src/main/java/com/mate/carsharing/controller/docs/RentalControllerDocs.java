package com.mate.carsharing.controller.docs;

import com.mate.carsharing.dto.rental.RentalCreateRequestDto;
import com.mate.carsharing.dto.rental.RentalDetailedResponseDto;
import com.mate.carsharing.dto.rental.RentalDto;
import com.mate.carsharing.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Rental Management", description = "Endpoints for managing car rentals")
public interface RentalControllerDocs {

    @Operation(
            summary = "Create rental",
            description = "Creates a rental for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rental created successfully",
                            content = @Content(schema = @Schema(implementation = RentalDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    RentalDto createRental(@RequestBody @Valid RentalCreateRequestDto rentalCreateRequestDto,
                           @RequestBody User user);

    @Operation(
            summary = "Return rental",
            description = "Marks the rental as returned for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental returned successfully",
                            content = @Content(schema = @Schema(implementation = RentalDto.class))),
                    @ApiResponse(responseCode = "404", description = "Rental not found"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    RentalDto returnRental(@PathVariable Long id, @RequestBody User user);

    @Operation(
            summary = "Get user rentals",
            description = "Returns rentals of the current user filtered by active status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of rentals",
                            content = @Content(schema = @Schema(implementation = RentalDto.class)))
            }
    )
    Page<RentalDto> getRentals(@RequestBody User user,
                               @RequestParam(defaultValue = "true") Boolean isActive,
                               Pageable pageable);

    @Operation(
            summary = "Get rental by ID",
            description = "Returns rental details. "
                    + "Managers can get any rental, users only their own",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental found",
                            content = @Content(schema = @Schema(
                                    implementation = RentalDetailedResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Rental not found"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    RentalDetailedResponseDto getRental(@PathVariable Long id, @RequestBody User user);

    @Operation(
            summary = "Get rentals by user ID",
            description = "Manager only: retrieves rentals by user ID filtered by active status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of rentals",
                            content = @Content(schema = @Schema(implementation = RentalDto.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    Page<RentalDto> getRentalsByUser(@PathVariable Long userId,
                                     @RequestParam(defaultValue = "true") Boolean isActive,
                                     Pageable pageable);

    @Operation(
            summary = "Get all rentals",
            description = "Manager only: retrieves all rentals",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of all rentals",
                            content = @Content(schema = @Schema(implementation = RentalDto.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    Page<RentalDto> getAllRentals(Pageable pageable);
}
