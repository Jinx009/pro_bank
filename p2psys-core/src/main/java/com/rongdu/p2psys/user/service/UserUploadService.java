package com.rongdu.p2psys.user.service;

import java.util.List;

import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserUpload;

/**
 * @author zf
 * @version 2.0
 * @since 2014年9月12日
 */
public interface UserUploadService {
    /**
     * 根据用户ID查询对应的实物照
     * @param 用户ID
     * @return 实物照
     */
    UserUpload findById(long id);

    /**
     * 根据用户查询对应的实物照集合
     * @param user
     * @return 实物照集合
     */
    List<UserUpload> findByUser(User user);


}
