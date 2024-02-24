package com.txd.project.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.txd.common.core.model.entity.InterfaceInfo;
import com.txd.common.core.model.entity.UserInterfaceInfo;
import com.txd.project.annotation.AuthCheck;
import com.txd.project.common.BaseResponse;
import com.txd.project.common.ErrorCode;
import com.txd.project.common.ResultUtils;
import com.txd.project.constant.UserConstant;
import com.txd.project.exception.BusinessException;
import com.txd.project.mapper.UserInterfaceInfoMapper;
import com.txd.project.model.vo.InterfaceInfoVO;
import com.txd.project.service.InterfaceInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tangx
 * @date 2024/2/24
 */

@RestController("/analysis")
public class AnalysisController {

    @Autowired
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        // 1.查询top N接口id和总调用次数
        List<UserInterfaceInfo> topInvokeInterfaceInfo = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        List<Long> topInvokeInterfaceIds = topInvokeInterfaceInfo.stream().map(UserInterfaceInfo::getInterfaceInfoId).collect(Collectors.toList());
        // 2.查询接口id对应的基本信息（包括了接口名称）
        List<InterfaceInfo> topInterfaceInfos = interfaceInfoService.listByIds(topInvokeInterfaceIds);
        if (CollectionUtil.isEmpty(topInterfaceInfos)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        // 3.将top N接口信息按照接口id分组,方便根据接口id拿接口总调用次数
        Map<Long, List<UserInterfaceInfo>> collect = topInvokeInterfaceInfo.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        // 4.将接口信息和调用次数转化为interfaceInfoVO对象
        return ResultUtils.success(topInterfaceInfos.stream().map(interfaceInfo ->{
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            interfaceInfoVO.setTotalNum(collect.get(interfaceInfoVO.getId()).get(0).getTotalNum());
            return interfaceInfoVO;
        }).collect(Collectors.toList()));
    }
}
