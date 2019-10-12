package com.daling.party.domain.busiess;

import com.daling.party.repository.dao.GroupDao;
import com.daling.party.domain.entity.Group;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GroupBusi {
    @Autowired
    private GroupDao groupDao;

    public Group getGroupById(Long id){
       return groupDao.selectByPrimaryKey(id);
    }
}
