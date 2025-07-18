/*
 * All Rights Reserved: Copyright [2024] [Zhuang Pan (paynezhuang@gmail.com)]
 * Open Source Agreement: Apache License, Version 2.0
 * For educational purposes only, commercial use shall comply with the author's copyright information.
 * The author does not guarantee or assume any responsibility for the risks of using software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izpan.modules.system.facade.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.izpan.common.util.CglibUtil;
import com.izpan.infrastructure.page.PageQuery;
import com.izpan.infrastructure.page.RPage;
import com.izpan.modules.system.domain.bo.SysRoleDataScopeBO;
import com.izpan.modules.system.domain.dto.role.data.scope.SysRoleDataScopeAddDTO;
import com.izpan.modules.system.domain.dto.role.data.scope.SysRoleDataScopeDeleteDTO;
import com.izpan.modules.system.domain.dto.role.data.scope.SysRoleDataScopeSearchDTO;
import com.izpan.modules.system.domain.dto.role.data.scope.SysRoleDataScopeUpdateDTO;
import com.izpan.modules.system.domain.entity.SysRoleDataScope;
import com.izpan.modules.system.domain.vo.SysRoleDataScopeVO;
import com.izpan.modules.system.facade.ISysRoleDataScopeFacade;
import com.izpan.modules.system.service.ISysRoleDataScopeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色数据权限关联管理 门面接口实现层
 *
 * @Author payne.zhuang <paynezhuang@gmail.com>
 * @ProjectName panis-boot
 * @ClassName com.izpan.modules.system.facade.impl.SysRoleDataScopeFacadeImpl
 * @CreateTime 2025-05-10 - 21:40:18
 */

@Service
@RequiredArgsConstructor
public class SysRoleDataScopeFacadeImpl implements ISysRoleDataScopeFacade {

    @NonNull
    private ISysRoleDataScopeService sysRoleDataScopeService;

    @Override
    public RPage<SysRoleDataScopeVO> listSysRoleDataScopePage(PageQuery pageQuery, SysRoleDataScopeSearchDTO sysRoleDataScopeSearchDTO) {
        SysRoleDataScopeBO sysRoleDataScopeBO = CglibUtil.convertObj(sysRoleDataScopeSearchDTO, SysRoleDataScopeBO::new);
        IPage<SysRoleDataScope> sysRoleDataScopeIPage = sysRoleDataScopeService.listSysRoleDataScopePage(pageQuery, sysRoleDataScopeBO);
        return RPage.build(sysRoleDataScopeIPage, SysRoleDataScopeVO::new);
    }

    @Override
    public SysRoleDataScopeVO get(Long id) {
        SysRoleDataScope byId = sysRoleDataScopeService.getById(id);
        return CglibUtil.convertObj(byId, SysRoleDataScopeVO::new);
    }

    @Override
    @Transactional
    public boolean add(SysRoleDataScopeAddDTO sysRoleDataScopeAddDTO) {
        SysRoleDataScopeBO sysRoleDataScopeBO = CglibUtil.convertObj(sysRoleDataScopeAddDTO, SysRoleDataScopeBO::new);
        return sysRoleDataScopeService.save(sysRoleDataScopeBO);
    }

    @Override
    @Transactional
    public boolean update(SysRoleDataScopeUpdateDTO sysRoleDataScopeUpdateDTO) {
        SysRoleDataScopeBO sysRoleDataScopeBO = CglibUtil.convertObj(sysRoleDataScopeUpdateDTO, SysRoleDataScopeBO::new);
        return sysRoleDataScopeService.updateById(sysRoleDataScopeBO);
    }

    @Override
    @Transactional
    public boolean batchDelete(SysRoleDataScopeDeleteDTO sysRoleDataScopeDeleteDTO) {
        SysRoleDataScopeBO sysRoleDataScopeBO = CglibUtil.convertObj(sysRoleDataScopeDeleteDTO, SysRoleDataScopeBO::new);
        return sysRoleDataScopeService.removeBatchByIds(sysRoleDataScopeBO.getIds(), true);
    }

    @Override
    public List<Long> listDataScopeIdsByRoleId(Long roleId) {
        // 调用服务层方法查询角色已配置的数据权限ID列表
        return sysRoleDataScopeService.listDataScopeIdsByRoleId(roleId);
    }

    @Override
    @Transactional
    public boolean addDataScopeForRoleId(Long roleId, List<Long> dataScopeIds) {
        // 调用服务层方法保存角色数据权限关联
        return sysRoleDataScopeService.addDataScopeForRoleId(roleId, dataScopeIds);
    }

}