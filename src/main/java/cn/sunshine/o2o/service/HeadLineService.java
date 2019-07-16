package cn.sunshine.o2o.service;

import cn.sunshine.o2o.entity.HeadLine;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-15 19:05
 */
public interface HeadLineService {

    /**
     * 查询头条列表，可查询条件：状态
     * @param headLineCondition
     * @return
     */
    List<HeadLine> getHeadLineList(HeadLine headLineCondition);

}
