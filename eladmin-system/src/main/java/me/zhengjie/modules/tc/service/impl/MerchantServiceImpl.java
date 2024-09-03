package me.zhengjie.modules.tc.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.tc.domain.Merchant;
import me.zhengjie.modules.tc.domain.TProvCityAreaStreet;
import me.zhengjie.modules.tc.repository.MerchantRepository;
import me.zhengjie.modules.tc.repository.TProvCityAreaStreetRepository;
import me.zhengjie.modules.tc.service.MerchantService;
import me.zhengjie.modules.tc.service.dto.MerchandiseQueryCriteria;
import me.zhengjie.modules.tc.service.dto.Region2Dto;
import me.zhengjie.modules.tc.service.dto.RegionDto;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TProvCityAreaStreetRepository tProvCityAreaStreetRepository;
    @Override
    public PageResult<Merchant> queryAll(MerchandiseQueryCriteria criteria, Pageable pageable) {
        Page<Merchant> page = merchantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }
    @Override
    public void modify(Merchant resources) {
        TProvCityAreaStreet province = tProvCityAreaStreetRepository.findBystateAndCode(1,resources.getProvinceId());
        resources.setProvince(province.getPointName());
        TProvCityAreaStreet city = tProvCityAreaStreetRepository.findBystateAndCode( 1,resources.getCityId());
        resources.setCity(city.getPointName());
        merchantRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        Set<Long> userIds=new HashSet<>();
        for (Long id : ids) {
            Merchant merchant = merchantRepository.findById(id).orElseGet(Merchant::new);
            if(StringUtils.isNotBlank(merchant.getState()) && Objects.equals(merchant.getState(), "2")){
                userIds.add(merchant.getUserId());
            }
        }
        if(!userIds.isEmpty()){
            userRepository.deleteAllByIdIn(userIds);
        }
        merchantRepository.deleteAllById(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Merchant resources) {
        User byUsername = userRepository.findByUsername(resources.getUserName());
        if(byUsername!=null){
            throw new BadRequestException("用户名已存在");
        }
        TProvCityAreaStreet province = tProvCityAreaStreetRepository.findBystateAndCode(1,resources.getProvinceId());
        resources.setProvince(province.getPointName());
        TProvCityAreaStreet city = tProvCityAreaStreetRepository.findBystateAndCode( 1,resources.getCityId());
        resources.setCity(city.getPointName());
        merchantRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(Merchant resources) {
       if(Objects.equals(resources.getState(), "2")){
           User user=new User();
           user.setUsername(resources.getUserName());
           user.setPassword(passwordEncoder.encode("123456"));
           user.setPhone(resources.getPhone());
           user.setEmail(resources.getUserName()+"@lw.com");
           user.setRoles(resources.getRoles());
           user.setDept(resources.getDept());
           user.setJobs(resources.getJobs());
           user.setNickName(resources.getUserName());
           user.setEnabled(true);
           user.setType(2);
           userRepository.save(user);
       }
        merchantRepository.saveAndFlush(resources);
    }

    @Override
    public  PageResult<RegionDto> getRegion(String value) {
        List<RegionDto> shengList = new ArrayList<>();
        if (value == null) {
            value = "0";
        }
        //获取省信息
        List<TProvCityAreaStreet> sheng = tProvCityAreaStreetRepository.findByParentIdAndState(value, 1);
        sheng.forEach(region -> {
            RegionDto dto = new RegionDto();
            dto.setLabel(region.getPointName());
            dto.setValue(region.getCode());
            shengList.add(dto);
        });


        return PageUtil.toPage(shengList,shengList.size());
    }

    @Override
    public PageResult<Region2Dto> getRegion() {
        List<Region2Dto> shengList = new ArrayList<>();
        List<TProvCityAreaStreet> sheng = tProvCityAreaStreetRepository.findByParentIdAndState("0", 1);
        sheng.forEach(region -> {
            Region2Dto dto = new Region2Dto();
            dto.setName(region.getPointName());
            dto.setValue(region.getCode());
            List<TProvCityAreaStreet> shi = tProvCityAreaStreetRepository.findByParentIdAndState(region.getId().toString(), 1);
            List<Region2Dto> shiList = new ArrayList<>();
            shi.forEach(shi1 -> {
                Region2Dto dto1 = new Region2Dto();
                dto1.setName(shi1.getPointName());
                dto1.setValue(shi1.getCode());
                shiList.add(dto1);
            });
            dto.setSubmenu(shiList);
            shengList.add(dto);
        });

        return PageUtil.toPage(shengList,shengList.size());
    }
}
