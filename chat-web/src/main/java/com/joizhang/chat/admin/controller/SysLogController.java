package com.joizhang.chat.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joizhang.chat.admin.api.dto.SysLogDTO;
import com.joizhang.chat.admin.api.entity.SysLog;
import com.joizhang.chat.admin.service.SysLogService;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.annotation.Inner;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/log")
@Tag(name = "日志管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * 简单分页查询
     *
     * @param page   分页对象
     * @param sysLog 系统
     * @return 日志分页
     */
    @GetMapping("/page")
    public R<IPage<SysLog>> getLogPage(Page<SysLog> page, SysLogDTO sysLog) {
        return R.ok(sysLogService.getLogByPage(page, sysLog));
    }

    /**
     * 删除日志
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_log_del')")
    public R<Boolean> removeById(@PathVariable Long id) {
        return R.ok(sysLogService.removeById(id));
    }

    /**
     * 插入日志
     *
     * @param sysLog 日志实体
     * @return success/false
     */
    @Inner
    @PostMapping
    public R<Boolean> save(@Valid @RequestBody SysLog sysLog) {
        return R.ok(sysLogService.save(sysLog));
    }

    /**
     * 导出excel 表格
     *
     * @param sysLog 查询条件
     * @return EXCEL
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_log_import_export')")
    public List<SysLog> export(SysLogDTO sysLog) {
        return sysLogService.getLogList(sysLog);
    }

}
