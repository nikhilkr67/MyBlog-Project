package com.myblog.controller;

import com.myblog.payload.CommentDTO;
import com.myblog.service.impl.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {

        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Object> createComment(
             @Valid
             @PathVariable("postId") long postId,
             @RequestBody CommentDTO commentDTO, BindingResult result
    ){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

      CommentDTO dto=commentService.createComment(postId,commentDTO);
      return new ResponseEntity<>(dto,HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO>getCommentByPostId(@PathVariable("postId")long postId){
        return commentService.getCommentsByPostId(postId);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @GetMapping("/posts/{postId}/comments/{id}")
   public ResponseEntity<CommentDTO>getCommentById(
           @PathVariable("postId")long postId,
           @PathVariable("id")long commentId){

        CommentDTO dto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDTO>updateComment(
            @PathVariable("postId")long postId,
            @PathVariable("id")long id,
            @RequestBody CommentDTO commentDTO
    ){
        CommentDTO dto = commentService.updateComment(postId,id,commentDTO);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String>deleteComment(
            @PathVariable("postId")long postId,
            @PathVariable("id") long id
    ){
        commentService.deleteComment(postId,id);
        return new ResponseEntity<>("Comment is delete",HttpStatus.OK);
    }
}
