package me.zhengjie.modules.tc.repository;

import me.zhengjie.modules.tc.domain.TProvCityAreaStreet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TProvCityAreaStreetRepository extends JpaRepository<TProvCityAreaStreet, Long>, JpaSpecificationExecutor<TProvCityAreaStreet> {

    /**
     * 查询省信息
     *
     * @param parentId 上级code
     * @param state    是否删除
     * @return 省信息
     */
    List<TProvCityAreaStreet> findByParentIdAndState(String parentId, int state);

    /**
     * 查询市区信息
     *
     * @param parentId 上级code
     * @param state    是否删除
     * @return 省信息
     */
    List<TProvCityAreaStreet> findByStateAndLevel(int state, int level);

    TProvCityAreaStreet findBystateAndCode(int state, String code);
}
