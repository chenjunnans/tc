package me.zhengjie.modules.tc.repository;

import me.zhengjie.modules.tc.domain.Category;
import me.zhengjie.modules.tc.service.dto.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Optional<Category> findByName(String name);

    List<Category> findByPid(Long pid);

    List<Category>  findByPidIsNullOrderBySort();

    List<Category> findByPidOrderBySort(Long pid);

    int countByPid(Long id);

    /**
     * 更新节点数目
     * @param count /
     * @param menuId /
     */
    @Modifying
    @Query(value = " update tc_category set sub_count = ?1 where id = ?2 ",nativeQuery = true)
    void updateSubCntById(int count, Long menuId);
}
