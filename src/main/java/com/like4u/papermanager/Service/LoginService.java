package com.like4u.papermanager.Service;

import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.pojo.User;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/5 14:20
 */

public interface LoginService {

    String login(User user);

    AjaxResult logout();
}
