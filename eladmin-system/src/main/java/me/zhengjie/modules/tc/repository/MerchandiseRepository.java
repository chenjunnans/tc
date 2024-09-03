package me.zhengjie.modules.tc.repository;

import me.zhengjie.modules.tc.domain.Merchandise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MerchandiseRepository extends JpaRepository<Merchandise, Long>, JpaSpecificationExecutor<Merchandise> {
    Optional<Merchandise> findByName(String name);

    List<Merchandise> findByCategoryId(Long categoryId);

    long  countByOpenId(String openId);
}
