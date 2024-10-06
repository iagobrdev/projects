package br.com.projects.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private LocalDateTime startDate;
    private String manager;
    private LocalDateTime forecast;
    private LocalDateTime endDate;
    private BigDecimal budget;
    private String description;
    private String status;
}