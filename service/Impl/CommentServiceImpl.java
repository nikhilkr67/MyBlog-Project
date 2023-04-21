package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.BlogApiException;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.CommentDTO;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{

    private PostRepository postRepository;

    private CommentRepository commentRepository;

    private ModelMapper mapper;

    public CommentServiceImpl(PostRepository postRepository,CommentRepository commentRepository,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper=mapper;
    }

    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO) {

        Comment comment= mapToEntity(commentDTO);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );

        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);

        CommentDTO dto = mapToDto(newComment);

        return dto;
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );



        List<Comment>comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("post","id",postId)
        );

        Comment comment= commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("comment","id",commentId)
        );

        if(comment.getPost().getId()!=post.getId()){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDTO updateComment(long postId, long id, CommentDTO commentDTO) {
        Post post= postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("post","id",postId)
        );

        Comment comment= commentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("comment","id",id)
        );

        if(comment.getPost().getId()!=post.getId()){
            throw  new BlogApiException(HttpStatus.BAD_REQUEST,"Post not matching with comment");
        }

        comment.setId(id);
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());


        Comment newComment = commentRepository.save(comment);
        return  mapToDto(newComment);

    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post= postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("post","id",postId)
        );

        Comment comment= commentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("comment","id",id)
        );

        if(comment.getPost().getId()!=post.getId()){
            throw  new BlogApiException(HttpStatus.BAD_REQUEST,"Post not matching with comment");
        }
        commentRepository.deleteById(id);
    }

    private CommentDTO mapToDto(Comment newComment) {
        CommentDTO commentDTO = mapper.map(newComment, CommentDTO.class);
        // CommentDTO commentDTO= new CommentDTO();
        //commentDTO.setId(newComment.getId());
        //commentDTO.setName(newComment.getName());
       // commentDTO.setEmail(newComment.getEmail());
       // commentDTO.setBody(newComment.getBody());

        return commentDTO;
    }

    private Comment mapToEntity(CommentDTO commentDTO) {
        Comment comment = mapper.map(commentDTO, Comment.class);
        //Comment comment= new Comment();
       // comment.setName(commentDTO.getName());
       // comment.setEmail(commentDTO.getEmail());
       // comment.setBody(commentDTO.getBody());
        return comment;
    }
}
