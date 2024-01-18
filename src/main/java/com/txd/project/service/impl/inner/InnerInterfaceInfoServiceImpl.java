package com.txd.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txd.common.core.model.entity.InterfaceInfo;
import com.txd.common.core.service.InnerInterfaceInfoService;
import com.txd.project.common.ErrorCode;
import com.txd.project.exception.BusinessException;
import com.txd.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author tangx
* @createDate 2023-07-31 16:14:07
*/
@Service
@DubboService
public class InnerInterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InnerInterfaceInfoService {

    @Autowired
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     *  通过请求路径和请求方法去数据库查询接口信息
     * @param url
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInvokeInterfaceInfo(String url, String method) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求路径或方法为空");
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}




