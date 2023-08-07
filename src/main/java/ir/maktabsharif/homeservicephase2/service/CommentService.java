package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.base.service.BaseService;
import ir.maktabsharif.homeservicephase2.entity.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService extends BaseService<Comment, Long> {

    @Override
    void save(Comment comment);

    @Override
    void delete(Comment comment);

    @Override
    Optional<Comment> findById(Long aLong);

    @Override
    List<Comment> findAll();
}
