package com.mate.carsharing.controller.docs;

import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Car Management", description = "Endpoints for managing cars")
public interface CarControllerDocs {

    @Operation(
            summary = "Create a new car",
            description = "Creates a new car (requires MANAGER role)",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Car created successfully",
                            content = @Content(schema = @Schema(implementation = CarDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden - insufficient privileges")
            }
    )
    CarDto createCar(@Valid @RequestBody CarCreateRequestDto requestDto);

    @Operation(
            summary = "Get all cars",
            description = "Retrieve a paginated list of all cars",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of cars",
                            content = @Content(schema = @Schema(implementation = CarDto.class)))
            }
    )
    Page<CarDto> getAllCars(Pageable pageable);

    @Operation(
            summary = "Get car by ID",
            description = "Retrieve a car by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Car retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CarDto.class))),
                    @ApiResponse(responseCode = "404", description = "Car not found")
            }
    )
    CarDto getCarById(@PathVariable Long id);

    @Operation(
            summary = "Update car",
            description = "Update an existing car (requires MANAGER role)",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Car updated successfully",
                            content = @Content(schema = @Schema(implementation = CarDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Car not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden - insufficient privileges")
            }
    )
    CarDto updateCar(@PathVariable Long id, @Valid @RequestBody CarUpdateRequestDto requestDto);

    @Operation(
            summary = "Delete car",
            description = "Delete a car by ID (requires MANAGER role)",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Car deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Car not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden - insufficient privileges")
            }
    )
    void deleteCar(@PathVariable Long id);
}
