package com.wsl.shoppingKill.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsl.shoppingKill.component.oss.OssComponent;
import com.wsl.shoppingKill.obj.constant.BaseEnum;
import com.wsl.shoppingKill.domain.Goods;
import com.wsl.shoppingKill.domain.Sku;
import com.wsl.shoppingKill.mapper.SkuMapper;
import com.wsl.shoppingKill.obj.vo.SkuVO;
import com.wsl.shoppingKill.service.SkuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * @author WangShilei
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService{

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private OssComponent ossComponent;

    @Override
    public SkuVO getSku(Long id) {
        return skuMapper.getSku(id);
    }

    @Override
    public IPage<SkuVO> getSkuAll(Long current, Long size, Long id, String name) {
        return skuMapper.getSkuAll(new Page<>(current,size),id,name);
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateSku(SkuVO skuVO) throws Exception {
        Sku sku = new Sku();
        Goods goods = new Goods();
        goods.setId(skuVO.getId()).setName(skuVO.getGoodsName()).updateById();
        if (!skuVO.getImg().isEmpty()){
            skuVO.setImgUrl(ossComponent.uploadFile(BaseEnum.OSS_SKU,skuVO.getImg()));
        }
        sku.setWarnNum(skuVO.getWarnNum())
                .setSellPrice(skuVO.getSellPrice())
                .setRealPrice(skuVO.getRealPrice())
                .setImgUrl(skuVO.getImgUrl())
                .setAttribute(skuVO.getAttribute())
                .setCostPrice(skuVO.getCostPrice())
                .setExpPrice(skuVO.getExpPrice())
                .setId(skuVO.getId())
                .setNum(skuVO.getNum())
                .setUpdateTime(LocalDateTime.now())
                .updateById();
        return true;
    }

    @Override
    public boolean delSku(Long id) {
        return skuMapper.deleteById(id)>0;
    }

    @Override
    public Integer getMaxNumByActivity(Long id) {
        return skuMapper.getMaxNumByActivity(id);
    }
}
