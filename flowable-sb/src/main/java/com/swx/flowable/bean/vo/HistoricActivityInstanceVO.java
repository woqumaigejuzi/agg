package com.swx.flowable.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricActivityInstanceVO {

    String processInstanceId;

    String activityId;

    String activityName;

    Long durationInMillis;

}
