package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.CommentRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.CommentResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.comment.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponseDTO convertToDTO(Comment comment) {
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setComment(comment.getTextComment());
        commentResponseDTO.setScore(comment.getScore());
        return commentResponseDTO;
    }

    public Comment convertToComment(CommentRequestDTO commentRequestDTO) {
        Comment comment = new Comment();
        comment.setTextComment(commentRequestDTO.getComment());
        comment.setScore(commentRequestDTO.getScore());
        return comment;
    }

}
