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

package com.izpan.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izpan.common.constants.SystemCacheConstant;
import com.izpan.common.util.CollectionUtil;
import com.izpan.infrastructure.page.PageQuery;
import com.izpan.infrastructure.util.RedisUtil;
import com.izpan.modules.system.domain.bo.SysDictBO;
import com.izpan.modules.system.domain.entity.SysDict;
import com.izpan.modules.system.repository.mapper.SysDictMapper;
import com.izpan.modules.system.service.ISysDictService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据字典管理 Service 服务接口实现层
 *
 * @Author payne.zhuang <paynezhuang@gmail.com>
 * @ProjectName panis-boot
 * @ClassName com.izpan.modules.system.service.impl.SysDictServiceImpl
 * @CreateTime 2024-06-27 - 22:03:29
 */

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public IPage<SysDict> listSysDictPage(PageQuery pageQuery, SysDictBO sysDictBO) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<SysDict>()
                .eq(ObjectUtils.isNotEmpty(sysDictBO.getName()), SysDict::getName, sysDictBO.getName())
                .eq(ObjectUtils.isNotEmpty(sysDictBO.getCode()), SysDict::getCode, sysDictBO.getCode());
        return baseMapper.selectPage(pageQuery.buildPage(), queryWrapper);
    }

    @Override
    public List<SysDict> getAllSysDict(SysDictBO sysDictBO) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<SysDict>()
                .eq(ObjectUtils.isNotEmpty(sysDictBO.getName()), SysDict::getName, sysDictBO.getName())
                .eq(ObjectUtils.isNotEmpty(sysDictBO.getCode()), SysDict::getCode, sysDictBO.getCode())
                .orderByAsc(SysDict::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysDict> getAllSysDict() {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<SysDict>()
                .orderByAsc(SysDict::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        List<Long> ids = CollectionUtil.toList(list, Long.class);
        List<SysDict> sysDictList = baseMapper.selectByIds(ids);
        boolean removed = super.removeBatchByIds(list, useFill);
        if (removed) {
            Set<String> keys = sysDictList.stream()
                    .map(item -> SystemCacheConstant.dictItemKey(item.getCode()))
                    .collect(Collectors.toSet());
            RedisUtil.del(keys);
        }
        return removed;
    }
}

