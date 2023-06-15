package com.joizhang.chat.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joizhang.chat.admin.api.entity.SysOauthClientDetails;
import com.joizhang.chat.admin.service.SysOauthClientDetailsService;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.log.annotation.RecordSysLog;
import com.joizhang.chat.common.security.annotation.Inner;
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
@RequestMapping("/client")
@Tag(name = "客户端管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysOauthClientDetailsController {

    private final SysOauthClientDetailsService sysOauthClientDetailsService;

    /**
     * 通过ID查询
     *
     * @param clientId 客户端id
     * @return SysOauthClientDetails
     */
    @GetMapping("/{clientId}")
    public R<List<SysOauthClientDetails>> getByClientId(@PathVariable String clientId) {
        LambdaQueryWrapper<SysOauthClientDetails> queryWrapper = Wrappers.<SysOauthClientDetails>lambdaQuery()
                .eq(SysOauthClientDetails::getClientId, clientId);
        return R.ok(sysOauthClientDetailsService.list(queryWrapper));
    }

    /**
     * 简单分页查询
     *
     * @param page                  分页对象
     * @param sysOauthClientDetails 系统终端
     * @return
     */
    @GetMapping("/page")
    public R<IPage<SysOauthClientDetails>> getOauthClientDetailsPage(Page<SysOauthClientDetails> page,
                                                                     SysOauthClientDetails sysOauthClientDetails) {
        return R.ok(sysOauthClientDetailsService.page(page, Wrappers.query(sysOauthClientDetails)));
    }

    /**
     * 添加
     *
     * @param sysOauthClientDetails 实体
     * @return success/false
     */
    @RecordSysLog("添加终端")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_client_add')")
    public R<Boolean> add(@Valid @RequestBody SysOauthClientDetails sysOauthClientDetails) {
        return R.ok(sysOauthClientDetailsService.save(sysOauthClientDetails));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @RecordSysLog("删除终端")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_client_del')")
    public R<Boolean> removeById(@PathVariable String id) {
        return R.ok(sysOauthClientDetailsService.removeClientDetailsById(id));
    }

    /**
     * 编辑
     *
     * @param sysOauthClientDetails 实体
     * @return success/false
     */
    @RecordSysLog("编辑终端")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_client_edit')")
    public R<Boolean> update(@Valid @RequestBody SysOauthClientDetails sysOauthClientDetails) {
        return R.ok(sysOauthClientDetailsService.updateClientDetailsById(sysOauthClientDetails));
    }

    @RecordSysLog("清除终端缓存")
    @DeleteMapping("/cache")
    @PreAuthorize("@pms.hasPermission('sys_client_del')")
    public R<Void> clearClientCache() {
        sysOauthClientDetailsService.clearClientCache();
        return R.ok();
    }

    @Inner
    @GetMapping("/getClientDetailsById/{clientId}")
    public R<SysOauthClientDetails> getClientDetailsById(@PathVariable String clientId) {
        LambdaQueryWrapper<SysOauthClientDetails> queryWrapper = Wrappers.<SysOauthClientDetails>lambdaQuery()
                .eq(SysOauthClientDetails::getClientId, clientId);
        return R.ok(sysOauthClientDetailsService.getOne(queryWrapper, false));
    }

}
