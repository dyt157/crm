package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.Clue;
import java.util.List;

public interface ClueMapper {
    int deleteClueByIds(String[] ids);

    int insertClue(Clue clue);

    Clue selectClueById(String id);

    List<Clue> selectAllClue();

    int updateClue(Clue clue);

    List<Clue> selectClueByCondition(Clue clue);


}