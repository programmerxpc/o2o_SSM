package cn.sunshine.o2o.dao;

import cn.sunshine.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-15 17:47
 */
public interface HeadLineDao {

    /**
     * 查询头条列表，可查询条件：状态
     * @param headLineCondition
     * @return
     */
    List<HeadLine> queryHeadLine(@Param("headLineCondition")HeadLine headLineCondition);

}
