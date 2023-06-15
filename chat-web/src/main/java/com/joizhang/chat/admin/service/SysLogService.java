package com.joizhang.chat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.admin.api.dto.SysLogDTO;
import com.joizhang.chat.admin.api.entity.SysLog;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 分页查询日志
     *
     * @param page the page
     * @param sysLog the SysLogDTO entity
     * @return 日志的分页
     */
    Page<SysLog> getLogByPage(Page<SysLog> page, SysLogDTO sysLog);

    /**
     * 列表查询日志
     *
     * @param sysLog 查询条件
     * @return List
     */
    List<SysLog> getLogList(SysLogDTO sysLog);

}
