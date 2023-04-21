package com.myblog.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PostDto {
    private Long id;

    @NotEmpty(message = "Is mandatory")
    @Size(min = 2,message = "Post title should have at least 2 characters")
    private  String title;

    @NotEmpty
    @Size(min = 10,message = "Post Description should have at least 10 characters")
    private String description;

    @NotEmpty
    @Size(min = 10, message = "Post content should be at least 10 characters")
    private String content;
}
