package ir.maktabsharif.homeservicephase2.base.repository;

import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;


public interface BaseRepository<E extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<E,ID> {

    @Override
     boolean existsById(ID id);


}
