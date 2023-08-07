package com.like4u.papermanager.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.like4u.papermanager.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/12 15:22
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(@Param("userId") Long userId);

    void  addUserWithVisitRole(@Param("userId")Long userId);
}
