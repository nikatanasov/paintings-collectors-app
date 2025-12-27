package com.paintingscollectors.web.dto;

import com.paintingscollectors.painting.model.Style;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaintingRequest {

    @NotNull(message = "Not null name!")
    @Size(min = 3, max = 20, message = "Name length must be between 5 and 40 characters!")
    private String name;

    @NotNull(message = "Not null author!")
    @Size(min = 3, max = 20, message = "Author length must be between 5 and 30 characters!")
    private String author;

    @NotNull(message = "Not null imageUrl!")
    @URL
    private String imageUrl;

    @NotNull(message = "Not null style!")
    private Style style;
}
