package com.joizhang.chat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joizhang.chat.admin.api.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
}
