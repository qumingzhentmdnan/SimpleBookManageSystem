package com.fjut.mapper;

import com.fjut.pojo.Feedback;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface FeedbackMapper {
    List<Feedback> getAllFeedback();
    boolean updateResInfo(@Param("responseInfo") String responseInfo, @Param("respId") Integer respId,@Param("responseTime") String responseTime);

    List<Feedback> queryFeedbackByKey(@Param("userId") Integer userId, @Param("feedbackInfo")String feedbackInfo,@Param("status") Integer status);

    boolean submitFeedback(Feedback feedback);
}
