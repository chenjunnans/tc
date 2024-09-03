package me.zhengjie.modules.tc.repository;

import me.zhengjie.modules.tc.domain.TProvCityAreaStreet;
import me.zhengjie.modules.tc.domain.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface WxUserRepository extends JpaRepository<WxUser, Long>, JpaSpecificationExecutor<WxUser> {
    Optional<WxUser> findByOpenid(String openid);
}
