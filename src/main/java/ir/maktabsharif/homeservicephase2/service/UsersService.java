package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.base.service.BaseService;
import ir.maktabsharif.homeservicephase2.entity.user.Users;

import java.util.Optional;

public interface UsersService<E extends Users> extends BaseService<E,Long> {

    Optional<E> findByUsername(String email);

    void editPassword(E e, String newPassword);

}
