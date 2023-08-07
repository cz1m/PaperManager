package com.like4u.papermanager.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.like4u.papermanager.Mapper.LoginMapper;
import com.like4u.papermanager.Mapper.MenuMapper;
import com.like4u.papermanager.domain.LoginUser;
import com.like4u.papermanager.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/12 15:21
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = loginMapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<String> list = menuMapper.selectPermsByUserId(user.getUserId());
        return new LoginUser(user, list);

    }

    public UserDetails loadUserByEmail(String username,String email) throws UsernameNotFoundException{
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
                .eq(User::getEmail,email);
        User user = loginMapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }List<String> list = menuMapper.selectPermsByUserId(user.getUserId());
        return new LoginUser(user, list);
    }
}
