package me.zhengjie.modules.tc.service.impl;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.tc.domain.Category;
import me.zhengjie.modules.tc.domain.Merchandise;
import me.zhengjie.modules.tc.domain.TProvCityAreaStreet;
import me.zhengjie.modules.tc.domain.WxUser;
import me.zhengjie.modules.tc.repository.*;
import me.zhengjie.modules.tc.service.MerchandiseService;
import me.zhengjie.modules.tc.service.dto.MerchandiseQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import net.dreamlu.mica.core.utils.StringUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchandiseServiceImpl implements MerchandiseService {

   private final MerchandiseRepository merchandiseRepository;

   private final MerchantRepository merchantRepository;
    private final TProvCityAreaStreetRepository tProvCityAreaStreetRepository;

    private final CategoryRepository catRepository;

    private final WxUserRepository wxUserRepository;
    @Override
    public PageResult<Merchandise> queryAll(MerchandiseQueryCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if(user.getUser().getType()!=1){
            //管理员
            criteria.setMerchantId(user.getUser().getId());
        }
        Page<Merchandise> page = merchandiseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        page.getContent().forEach(merchandise -> {
            if(merchandise.getMerchantId()==null){
                merchandise.setMerchant("官方自营");
            }else{
                merchantRepository.findByUserId(merchandise.getMerchantId()).ifPresent(merchant -> {
                    merchandise.setMerchant(merchant.getName());
                });
            }
        });
        return PageUtil.toPage(page);
    }

    @Override
    public PageResult<Merchandise> queryAll2(MerchandiseQueryCriteria criteria, Pageable pageable) {
        if(criteria.getCategoryId()==null||criteria.getCategoryId()==0){
            criteria.setCategoryId(null);
        }
        Page<Merchandise> page = merchandiseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        page.getContent().forEach(merchandise -> {
            if(merchandise.getUrl()!=null){
                String[] urls = merchandise.getUrl().split(",");
                merchandise.setUrls(urls);
            }
            if(merchandise.getOpenId()==null){
                merchandise.setMerchant("官方自营");
            }else{
//                merchantRepository.findByUserId(merchandise.getMerchantId()).ifPresent(merchant -> {
//                    merchandise.setMerchant(merchant.getName());
//                });
                merchandise.setMerchant("用户发布");
            }
//            if(merchandise.getDiQu()!=null){
//                List<String> resultList = JSONUtil.toList(merchandise.getDiQu(), List.class).stream()
//                        .map(subArray -> ((List<String>) subArray).get(((List<String>) subArray).size() - 1)).collect(Collectors.toList());
//                List<String> list = new ArrayList<>();
//                resultList.forEach(s -> {
//                    TProvCityAreaStreet tc = tProvCityAreaStreetRepository.findBystateAndCode(1, s);
//                    list.add(tc.getPointName());
//                });
//                merchandise.setDiQu(StringUtils.join(list, ','));
//            }
            Category category = catRepository.findById(merchandise.getCategoryId()).orElse(new Category());
            merchandise.setCategoryName(category.getName());
        });
        return PageUtil.toPage(page.getContent(),page.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Merchandise resources) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        resources.setMerchantId(user.getUser().getId());
        merchandiseRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Merchandise resources) {
        merchandiseRepository.saveAndFlush(resources);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        merchandiseRepository.deleteAllById(ids);
    }

    @Override
    @Transactional
    public Merchandise query(Long id) {
        Merchandise merchandise = merchandiseRepository.findById(id).orElseThrow(() -> new RuntimeException("商品不存在"));
        String[] urls = merchandise.getUrl().split(",");
        merchandise.setUrls(urls);
        if(StringUtil.isBlank(merchandise.getOpenId())){
            merchandise.setMerchant("官方发布");
        }else{
            merchandise.setMerchant("用户发布");
        }
        String diQu=merchandise.getDiQu();
        List<String> resultList = JSONUtil.toList(diQu, List.class).stream()
                .map(subArray -> ((List<String>) subArray).get(((List<String>) subArray).size() - 1)).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        resultList.forEach(s -> {
            TProvCityAreaStreet tc = tProvCityAreaStreetRepository.findBystateAndCode(1, s);
            list.add(tc.getPointName());
        });
        merchandise.setDiQu2(StringUtils.join(list, ','));
        merchandise.setDiQu(diQu);
        return merchandise;
    }

    @Override
    @Transactional
    public void create2(Merchandise resources) {
        WxUser wxUser = wxUserRepository.findByOpenid(resources.getOpenId()).orElse(new WxUser());
        long l = merchandiseRepository.countByOpenId(resources.getOpenId());
        if(l<wxUser.getNumber()){
            resources.setStatus(2);
            merchandiseRepository.save(resources);
        }else{
            throw new BadRequestException("发布消息数量已达上限!");
        }

    }

    @Override
    @Transactional
    public void delete2(Long id) {
        merchandiseRepository.deleteById(id);
    }

}
