package me.zhengjie.modules.tc.repository;

import me.zhengjie.modules.tc.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MerchantRepository  extends JpaRepository<Merchant, Long>, JpaSpecificationExecutor<Merchant> {

    Optional<Merchant> findByUserId(Long userId);
}
