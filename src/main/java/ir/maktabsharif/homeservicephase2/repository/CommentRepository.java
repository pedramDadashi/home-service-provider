package ir.maktabsharif.homeservicephase2.repository;


import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.comment.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends BaseRepository<Comment,Long> {

//    @Query("select c from Comment c where c.order= :expert")
//    List<Comment> findByExpert(Worker worker);

}
